package com.msb.project;

/*
 * Copyright © 2023 runa6785 <runa6785@gmail.com>
 *
 * BotMain.java
 * 무스비
 */

import java.awt.Color;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class Main { 	
	// 중복 출력 방지 변수
	public static boolean isText = true;
	
	// 반응 횟수 변수
	public static int TextCount = 0; 
	
	static Maple Maple = new Maple();
    
    static String token = "";
    static DiscordApi api;
    
	public static void main (String [] args) {	
		System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe"); // 윈도우
		//System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver"); // 리눅스
			
		api = new DiscordApiBuilder()
			.setToken(token)
			.setAllIntents()
			.login()
			.join();
		api.addMessageCreateListener(new MessageCreateListener() { 
			public void onMessageCreate(MessageCreateEvent event) {
    			if (TextCount % 25 == 0) { // 25번 반응할때마다 변경
    				double RandomValue = Math.random();
    				int TextRandom = (int)(RandomValue * 9);
    				
    				if (TextRandom <= 4) {
        				api.updateActivity(ActivityType.WATCHING, "메이플스토리 유튜브");
        			} else  {
        				api.updateActivity(ActivityType.PLAYING, "메이플스토리");
        			}
    			}
     			
    			if (event.getMessageAuthor().getName().contentEquals("무스비")) {
				// 봇은 채팅 감지 X
    			} else {
    				if (event.getMessageContent().contentEquals("#명령어")) {
    					new MessageBuilder()
    					.setEmbed(new EmbedBuilder()
						.setTitle("명령어 목록")
						.setDescription(
							"#정보 (닉네임)" + "\n" +
							"#레벨 (닉네임)" + "\n" +
							"#유니온 (닉네임)" + "\n" +
							"#업적 (닉네임)" + "\n" +
							"#무릉 (닉네임)" + "\n" +
							"#더시드 (닉네임)" + "\n" +
							"#링크 (직업)")
						.setColor(Color.RED))
						.send(event.getChannel());
						
						TextCount++;
            		} else { // 채팅 확인     
            			if (!isText) {
            				isText = true;
            			}
            			
            			if (isText) { // 메이플
            				Maple.ChatEquals(event);
            			}
            		}
    			}
			}
		});
	}
}
