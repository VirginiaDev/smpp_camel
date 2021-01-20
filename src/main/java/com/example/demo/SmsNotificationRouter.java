package com.example.demo;

import com.example.demo.dto.Clients;
import com.example.demo.dto.Message;
import com.example.demo.processor.MessageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.apache.camel.Processor;
import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.smpp.SmppException;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationRouter extends RouteBuilder {

//	@Autowired
//	DataSource dataSource;
//	
	@BeanInject
	private MessageProcessor messageProcessor;
//	
//	public DataSource getDataSource() {
//		return dataSource;
//	}
//
//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}
//	
	@Bean
	public SqlComponent sql(DataSource dataSource) {
		SqlComponent sql = new SqlComponent();
		sql.setDataSource(dataSource);
		return sql;
	}

	@PropertyInject(value = "local.country", defaultValue = "IN")
	private String localCountry="IN";
	
	@PropertyInject(value = "smsc.country", defaultValue = "IN")
	private String smscCountry="IN";
	
	@PropertyInject(value = "blacklist.countries", defaultValue = "CI;GF;GP;MQ;NC;PF;RE")
	private String blacklistCountries;
	
	@PropertyInject(value = "source-overrides", defaultValue = "IN,IN:919780568455")
	private String overrides;
	
	@PropertyInject(value = "throttle.timePeriodMillis", defaultValue = "1000")
	long throttleTimePeriodMillis;
	
	@PropertyInject(value = "throttle.maximumRequestsPerPeriod", defaultValue = "1")
	int throttleRequestsPerPeriod;

	@Override
	public void configure() throws Exception {
		CountryClassifier origin =
				new CountryClassifier("IN", "SMSOrigin");
		CountryClassifier destination =
				new CountryClassifier("IN", "SMSDestination");
		Predicate blacklistedDestination = new Blacklist("CI;GF;GP;MQ;NC;PF;RE",
				"SMSDestinationCountryISO2");
		SourceOverride sourceOverride = new SourceOverride("IN:+917009073863",
				destination.getCountryHeaderName(), origin.getParsedHeaderName());
		SmppAddressing smppAddressing = new SmppAddressing("IN",
				origin.getParsedHeaderName(), destination.getParsedHeaderName());
		
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

//		rest("/message")
//				.post("/add").consumes(MediaType.APPLICATION_JSON_VALUE).type(Message.class).outType(Message.class)
//				.to("seda:newMessage");


		from("direct:select").to("sql:select * from clients where status = 1").process(new Processor() {
			public void process(Exchange xchg) throws Exception {
			//the camel sql select query has been executed. We get the list of employees.
				ArrayList<Map<String, String>> dataList = (ArrayList<Map<String, String>>) xchg.getIn().getBody();
				List<Clients> list = new ArrayList<Clients>();
				System.out.println(dataList);
				for (Map<String, String> data : dataList) {
					Clients c = new Clients();
					c.setId(Integer.parseInt(data.get("id")));
					c.setSender_details(data.get("sender_details"));
					list.add(c);
				}
				xchg.getIn().setBody(list);
			}
		});
	
		
		from("seda:newMessage?concurrentConsumers=20").routeId("smpp-sender").process(messageProcessor)
				.setHeader("CamelSmppDestAddr",simple("91${in.body.sender}"))
				.setBody(simple("${in.body.messageBody}"))
				.to("smpp://{{smpp.tr.systemid}}@{{smpp.tr.host}}:{{smpp.tr.port}}?password={{smpp.tr.password}}&enquireLinkTimer=3000&transactionTimer=5000&sourceAddrTon={{smpp.source.addr.ton}}&sourceAddrNpi={{smpp.source.addr.npi}}&destAddrTon={{smpp.dest.addr.ton}}&destAddrNpi={{smpp.dest.addr.npi}}&sourceAddr={{smpp.source.address}}");

//		String smppUriTemplate =
//				"smpp://Saless@182.18.144.246:8585"
//				+ "?password=Sales12"
//				+ "&systemType=cp"
//				+ "&enquireLinkTimer=3000"
//				+ "&typeOfNumber=1"
//				+ "&numberingPlanIndicator=1";
//
//		String smppUriProducer = smppUriTemplate + "&registeredDelivery=1";
//		String smppUriConsumer = smppUriTemplate;
//		
//		String dlq = 
//				"smpp://testing_account@182.18.144.246:8585"
//						+ "?password=Test12"
//						+ "&systemType=cp"
//						+ "&enquireLinkTimer=3000"
//						+ "&typeOfNumber=1"
//						+ "&numberingPlanIndicator=1";
		
		/**
		 * This Camel route handles messages going out to the SMS world
		 */
//		from("seda:newMessage?concurrentConsumers=20")
//			.errorHandler(deadLetterChannel(dlq))
//			.onException(SmppException.class)
//				.maximumRedeliveries(0)
//				.end()
//			.removeHeaders("CamelSmpp*")//In case it started as SMS elsewhere
//			.process(origin)
//			.process(destination)
//			.choice()
//				.when(blacklistedDestination)
//					.to(dlq)
//				.otherwise()
//					.process(sourceOverride)
//					.process(smppAddressing)
//					.throttle(throttleRequestsPerPeriod)
//						.timePeriodMillis(throttleTimePeriodMillis)
//					//.to("mock:foo")
//					.setBody(simple("The SMSC accepted the message"
//							+ " for 917009073863"
//							+ " and assigned SMPP ID: TEST"))
//					.to(smppUriProducer);
	}
	
	public static void main(String args[]) {
		try {
			new SmsNotificationRouter().configure();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
