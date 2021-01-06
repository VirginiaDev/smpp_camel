package com.example.demo;

import java.util.HashMap;

public class GetAllMessages {
	public static HashMap<String, Object> messages = new HashMap<String, Object>();
	
	public static void setMessages() {
		messages.put("919876765432", "Hello James");
		messages.put("917768765456", "New Year Coming");
		messages.put("918789677876", "Sat Sri Akkal");
		messages.put("918767545676", "Come On");
		messages.put("918921781276", "Let's Meet");
		messages.put("918789821245", "How's U");
		messages.put("919988945432", "Kiwwee a");
		messages.put("918787665544", "Whats Going on");
		messages.put("918765678765", "How's Work");
		messages.put("918767855443", "Goooo");
		messages.put("918767855400", "Akhtar Ji");
		messages.put("919917898765", "Hello Google");
		messages.put("918944881198", "Whats New");
		messages.put("917876510926", "half Heart");
	}
	
	public HashMap<String, Object> getMessages() {
		setMessages();
		return messages;
	}

}
