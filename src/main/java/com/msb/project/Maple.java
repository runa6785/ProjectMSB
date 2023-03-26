package com.msb.project;

/*
 * Copyright © 2023 runa6785 <runa6785@gmail.com>
 *
 * Maple.java
 * 메이플 관련
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Maple {
	// URL
	String MapleInfoUrl = "https://maple.gg/u/";
	String MapleLeveUrl = "https://maplestory.nexon.com/Ranking/World/Total?c=";
	
	// 이름
	String CharName = null;
	String CNTemp = null;
	
	// 크롤링 관련 변수
	ChromeOptions options = new ChromeOptions();
	String ElementsTemp = null;
	Boolean IsName = false;
	
	// #정보 관련 
	File scrFile;
	int CharCount = 0;
	
	// #레벨 관련
	Boolean IsLevel = false;
	
	String MapleJob = null;
	String MapleLevel = null;
	String MapleExp = null;
		
	// #유니온 관련
	Boolean IsUnion = false;
	
	int UCoin = 0;
	
	String ULevel = null;
	String UGrade = null;
	String UPower = null;
	String UDate = null;
	String UImg = null;
	
	// #업적 관련
	Boolean IsAchievement = false;
	
	String ALevel = null;
	String AGrade = null;
	String ADate = null;
	String AImg = null;
	
	// #무릉 관련
	Boolean IsMulung = false;
	
	int MGrade = 0;

	String MTime = null;
	String MLevel = null;
	String MJob = null;
	String MWRank = null;
	String MRank = null;
	String MDate = null;
	
	// 10층 마다 줄 넘김
	String[] MobData = { // 몬스터 이름
			"0층", "마노", "머쉬맘", "스텀피", "블루 머쉬맘", "좀비 머쉬맘", "킹슬라임", "다일", "킹크랑", "파우스트", "반 레온",
			"메탈 골렘", "포장마차", "주니어 발록", "엘리쟈", "크림슨 발록", "설산의 마녀", "세르프", "데우", "파파픽시", "힐라",
			"디트와 로이 + 네오 휴로이드", "키메라 + 호문스큘러", "프랑켄로이드 + 미스릴 뮤테", "차우 + 원시멧돼지", "에피네아 + 샤이닝 페어리", "롬바드 + 킹 블록골렘", "타이머 + 틱톡", "마스터 스펙터 + 사신 스펙터", "마스터 버크 + 듀얼 버크", "아카이럼",
			"마뇽 × 2", "그리프 × 2", "크세르크세스 × 2", "파풀라투스 × 2", "알리샤르 × 2", "스노우맨 × 2", "리치 × 2", "아니 × 3", "킹 오멘 × 3", "매그너스",
			"타르가", "슼갈리온", "요괴선사", "데비존", "라바나", "레비아탄", "도도", "릴리노흐", "라이카", "핑크빈",
			"락 스피릿", "타란튤로스", "드래고니카", "드래곤 라이더", "호크아이", "이카르트", "이리나", "오즈", "미하일", "시그너스",
			"피아누스", "렉스", "카웅", "변형된 스텀피", "하늘 수호령", "게오르크", "타락마족 강화늑대기수", "아르마", "츄릅나무", "스우",
			"클리너", "약화된 조화의 정령", "증발하는 에르다스", "아랑", "봉선", "오공", "송달", "황룡", "적호", "무공",
			"아라네아", "빛의 집행자", "히아데스", "공허의 하수인", "데미안", "황혼의 하수인", "거대한 골렘", "리버스 다크 소울", "절망의 칼날", "윌",
			"안세스티온", "어센시온", "엠브리온", "각성한 아랑", "각성한 봉선", "각성한 오공", "각성한 송달", "각성한 황룡", "각성한 적호", "각성한 무공"
			};
	String[] MobLevel = { // 몬스터 레벨
			"0", "100", "102", "104", "106", "108", "110", "113", "115", "118", "120",
			"120", "123", "125", "128", "130", "132", "138", "138", "140", "143",
			"145", "148", "150", "153", "155", "158", "160", "163", "165", "168",
			"170", "175", "180", "185", "190", "195", "200", "205", "210", "215",
			"220", "220", "220", "220", "220", "230", "230", "230", "230", "230",
			"240", "240", "240", "240", "240", "245", "245", "245", "245", "245",
			"250", "250", "250", "250", "250", "255", "255", "255", "255", "255",
			"260", "260", "260", "260", "260", "260", "260", "260", "260", "260",
			"265", "265", "265", "265", "265", "265", "265", "265", "265", "265",
			"270", "270", "270", "270", "270", "270", "270", "270", "270", "275"
			};
	String[] MobHp = { // 몬스터 체력
			"0", "5,200,000", "5,740,800", "6,307,200", "6,930,000", "7,549,200", "12,342,000", "13,923,000", "15,105,000", "16,846,000", "100,000,000",
			"40,824,000", "45,404,550", "48,593,250", "55,350,000", "61,600,500", "68,121,000", "78,840,000", "90,011,250", "97,902,000", "1,500,000,000",
			"130,536,000", "159,138,000", "190,350,000", "242,424,000", "405,504,000", "497,040,000", "596,496,000", "706,176,000", "824,256,000", "3,000,000,000",
			"4,216,480,000", "5,053,040,000", "5,952,000,000", "6,929,840,000", "7,973,280,000", "9,102,000,000", "10,299,520,000", "19,424,880,000", "23,915,520,000", "8,000,000,000",
			"42,000,000,000", "63,000,000,000", "84,000,000,000", "105,000,000,000", "105,000,000,000", "210,000,000,000", "315,000,000,000", "420,000,000,000", "525,000,000,000", "525,000,000,000",
			"630,000,000,000", "735,000,000,000", "840,000,000,000", "945,000,000,000", "1,050,000,000,000", "1,155,000,000,000", "1,260,000,000,000", "1,365,000,000,000", "1,470,000,000,000", "1,575,000,000,000",
			"1,680,000,000,000", "1,785,000,000,000", "1,890,000,000,000", "1,911,000,000,000", "1,932,000,000,000", "1,953,000,000,000", "1,954,000,000,000", "1,995,000,000,000", "2,016,000,000,000", "2,100,000,000,000",
			"2,310,000,000,000", "2,625,000,000,000", "2,940,000,000,000", "3,255,000,000,000", "3,570,000,000,000", "3,915,000,000,000", "4,210,000,000,000", "4,535,000,000,000", "4,857,000,000,000", "5,257,300,000,000",
			"5,790,900,000,000", "6,099,800,000,000", "6,426,100,000,000", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
			};
	String[] MobAcHp = { // 몬스터 누적 체력
			"0", "5,200,000", "10,940,800", "17,248,000", "24,178,000", "31,727,200", "44,069,200", "57,992,200", "73,097,200", "89,943,200", "280,419,390",
			"321,243,390", "366,647,940", "415,241,190", "470,591,190", "532,191,690", "600,312,690", "679,152,690", "769,163,940", "867,065,940", "3,724,208,798",
			"3,854,744,798", "4,013,882,798", "4,204,232,798", "4,446,656,798", "4,852,160,798", "5,349,200,798", "5,945,696,798", "6,651,872,798", "7,476,128,798", "13,190,414,512",
			"17,406,894,512", "22,459,934,512", "28,411,934,512", "35,341,774,512", "43,315,054,512", "52,417,054,512", "62,716,574,512", "82,141,454,512", "106,056,974,512", "121,295,069,750",
			"163,295,069,750", "226,295,069,750", "310,295,069,750", "415,295,069,750", "520,295,069,750", "730,295,069,750", "1,045,295,069,750", "1,465,295,069,750", "1,990,295,069,750", "2,990,295,069,750",
			"3,620,295,069,750", "4,355,295,069,750", "5,195,295,069,750", "6,140,295,069,750", "7,190,295,069,750", "8,345,295,069,750", "9,605,295,069,750", "10,970,295,069,750", "12,440,295,069,750", "15,440,295,069,750",
			"17,120,295,069,750", "18,905,295,069,750", "20,795,295,069,750", "22,706,295,069,750", "24,638,295,069,750", "26,591,295,069,750", "28,565,295,069,750", "30,560,295,069,750", "32,576,295,069,750", "36,576,295,069,750",		
			"38,886,295,069,750", "41,511,295,069,750", "44,451,295,069,750", "47,706,295,069,750", "51,276,295,069,750", "55,191,295,069,750", "59,401,295,069,750", "63,936,295,069,750", "68,793,295,069,750", "78,807,199,831,654",	
			"84,598,099,831,654", "90,697,899,831,654", "97,123,999,831,654", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
			};
	String[] MobDef = { // 몬스터 방어율
			"0", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%",
			"50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%",
			"50%/10%", "50%/10%", "50%/10%", "50%/10%", "50%/10%", "50%/10%", "50%/10%", "50%/10%", "50%/10%", "50%",
			"50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%",
			"50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%",
			"50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "50%", "100%",
			"100%", "100%", "100%", "100%", "100%", "100%", "100%", "100%", "100%", "150%",
			"150%", "150%", "150%", "150%", "150%", "150%", "150%", "150%", "150%", "200%",
			"200%", "200%", "200%", "200%", "200%", "200%", "200%", "200%", "200%", "250%",
			"250%", "250%", "250%", "250%", "250%", "250%", "250%", "250%", "250%", "300%"
			};
	
	// #더시드 관련
	Boolean IsSeed = false;
	
	String SGrade = null;
	String STime = null;
	String SWRank = null;
	String SRank = null;
	String SDate = null;
	
	// #링크 관련
	String JobName = null;
	EmbedBuilder embed;
	
	public void MapleggImg() { // 셀레니움 크롤링
		IsName = false;
		
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>(); 
		chromePrefs.put("download.default_directory", "photo/Maplegg"); // 윈도우
		//chromePrefs.put("download.default_directory", "/home/ec2-user/SON7BOT/photo/Maplegg"); // 리눅스
		
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--headless"); 
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-gpu"); 
		options.addArguments("--no-sandbox"); 
		options.addArguments("--disable-dev-shm-usage"); 
        options.setExperimentalOption("prefs", chromePrefs);
        
		WebDriver driver = new ChromeDriver(options);
		try {
			driver.get(MapleInfoUrl + CharName);	

			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
	        Long offsetTop = (Long) jsExecutor.executeScript(
	        		"window.scroll(0, document.querySelector(\"" + "#character-card" + "\").offsetTop - 0);"
    				+ "return document.querySelector(\"" + "#character-card" + "\").getBoundingClientRect().top;");
	        
			driver.findElement(By.xpath("//*[@id=\"user-profile\"]/section/div[2]/div[2]/div/div[4]/button[2]")).click();
			
			Thread.sleep(1000);	
			WebElement element = driver.findElement(By.cssSelector("#character-card"));	
			
			File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        BufferedImage  fullImg = ImageIO.read(screenshot);

	        Point point = element.getLocation();

	        int eleWidth = element.getSize().getWidth();
	        int eleHeight = element.getSize().getHeight();

	        BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), Math.toIntExact(offsetTop + 25), eleWidth, eleHeight);
	        ImageIO.write(eleScreenshot, "png", screenshot);

	        File screenshotLocation = new File("photo/Maplegg/CharName" + CharCount + ".png"); // 윈도우
	        //File screenshotLocation = new File("/home/ec2-user/SON7BOT/photo/Maplegg/CharName" + CharCount + ".png"); // 리눅스
	        FileUtils.copyFile(screenshot, screenshotLocation);
	        
			Thread.sleep(1000);		
		} catch (Exception e) {
			e.printStackTrace();
			IsName = true;
		} finally {
			driver.quit();
		}	
	}
	
	public void MapleInfo() { // Jsoup 크롤링
		IsName = false;
		
		MapleLevel = null;
        MapleJob = null;
        
		ULevel = null;
		UGrade = null;
		UPower = null;
		UDate = null;
		
		ALevel = null;	
		AGrade = null;
		ADate = null;
		
		MGrade = 0;
		MTime = null;
		MLevel = null;
		MJob = null;
		MWRank = null;
		MRank = null;
		MDate = null;
		
		SGrade = null;
		STime = null;
		SWRank = null;
		SRank = null;
		SDate = null;
		
		// 이미지
		UImg = null;
		AImg = null;
        
		Connection Maplegg = Jsoup.connect(MapleInfoUrl + CharName);
		if (IsLevel == true) { // # 레벨
			IsLevel = false;
			
			Connection MapleHome = Jsoup.connect(MapleLeveUrl + CharName);
			try {
				Document Homedocument = MapleHome.get();
				Elements MapleHomeElements = Homedocument.getElementsByClass("search_com_chk");
				
				CNTemp = CharName;
				
				if (MapleHomeElements.size() == 0) {
					Connection MapleRHome = Jsoup.connect(MapleLeveUrl + CharName + "&w=254");
					Homedocument = MapleRHome.get();
					MapleHomeElements = Homedocument.getElementsByClass("search_com_chk");
					
					CNTemp = CharName + " - " + "리부트";
				}	
				
				String HomeTemp = MapleHomeElements.get(0).text();
				String[] HomeArr = HomeTemp.split(" ");	
				
				MapleJob = HomeArr[3] + " / " + HomeArr[5];
				MapleExp = HomeArr[7];
				
				Document ggdocument = Maplegg.get();
				Elements MapleLevelElements = ggdocument.getElementsByClass("user-summary-item");
				MapleLevel = MapleLevelElements.get(0).text();
			} catch (IOException e) {
				IsName = true;
			}
		} else if (IsUnion == true) { // #유니온
			IsUnion = false;
			
			try {
				Document Homedocument = Maplegg.get();
				Elements MapleggElements = Homedocument.getElementsByClass("box user-summary-box");
				Elements MapleImgElements = Homedocument.getElementsByClass("user-summary-tier");
				
				String GGTemp = MapleggElements.get(2).text();
				String[] GGArr = GGTemp.split(" ");	
				
				UGrade = GGArr[1] + " " + GGArr[2];
				ULevel = GGArr[3];
				ULevel.replace("Lv.", "");
				UPower = GGArr[5];
				
				UDate = GGTemp.substring(GGTemp.indexOf("기준일:"));
				UImg = MapleImgElements.get(0).attr("abs:src");
			} catch (IOException e) {
				IsName = true;
			}
		} else if (IsAchievement == true) { // #업적
			IsAchievement = false;
			
			try {
				Document Homedocument = Maplegg.get();
				Elements MapleggElements = Homedocument.getElementsByClass("box user-summary-box");
				Elements MapleImgElements = Homedocument.getElementsByClass("user-summary-tier");
				
				String GGTemp = MapleggElements.get(3).text();
				String[] GGArr = GGTemp.split(" ");	
				
				AGrade = GGArr[1];
				ALevel = GGArr[3];
				ALevel.replace("Lv.", "");
				
				ADate = GGTemp.substring(GGTemp.indexOf("기준일:"));
				AImg = MapleImgElements.get(0).attr("abs:src");
			} catch (IOException e) {
				IsName = true;
			}
		} else if (IsMulung == true) { // #무릉
			IsMulung = false;
			
			try {
				Document Homedocument = Maplegg.get();
				Elements MapleggElements = Homedocument.getElementsByClass("box user-summary-box");
				Elements MapleggOldElements = Homedocument.getElementsByClass("old-dojang");
				
				String GGTemp = MapleggElements.get(0).text();
				String[] GGArr = GGTemp.split(" ");	
				
				MGrade = Integer.parseInt(GGArr[2]);
				MTime = GGArr[4] + " " + GGArr[5];
				
				if (MapleggOldElements.size() >= 1) {
					MLevel = GGArr[10];
					MLevel.replace("Lv.", "");
					MJob = GGArr[12];	
					MWRank = GGArr[14];
					MRank = GGArr[16];
				} else {
					MLevel = GGArr[6];
					MLevel.replace("Lv.", "");
					MJob = GGArr[8];
					MWRank = GGArr[10];
					MRank = GGArr[12];
				}
				MDate = GGTemp.substring(GGTemp.indexOf("기준일:"));
			} catch (IOException e) {
				IsName = true;
			}
		} else if (IsSeed == true) { // #시드
			IsSeed = false;
			
			try {
				Document Homedocument = Maplegg.get();
				Elements MapleggElements = Homedocument.getElementsByClass("box user-summary-box");
				
				String GGTemp = MapleggElements.get(1).text();
				String[] GGArr = GGTemp.split(" ");	

				SGrade = GGArr[2] + "층";
				STime = GGArr[4] + " " + GGArr[5];
				SWRank = GGArr[10] + "위";
				SRank = GGArr[13];
				
				SDate = GGTemp.substring(GGTemp.indexOf("기준일:"));
			} catch (IOException e) {
				IsName = true;
			}
		} 
	}
	
	public void ChatEquals(MessageCreateEvent event) {
		// 명령어 예외 처리
		if (event.getMessageContent().contentEquals("#정보")) {
			new MessageBuilder()
			.setEmbed(new EmbedBuilder()
	            .setTitle("#정보")
	            .setDescription(
            		"#정보 (닉네임)을 입력하여 프로필을 확인할 수 있습니다." + "\n" +
    				"상세 정보 확인을 위해서는 #정보 대신 #무릉, #유니온, #업적 을 입력해주세요." + "\n" +
    				" " + "\n" +
    				"자세한 정보는 #명령어 메이플 / 메이플스토리 명령어로 확인할 수 있습니다." + "\n" +
    				" " + "\n")
	            .setColor(Color.GREEN))
		    .send(event.getChannel());
		} else if (event.getMessageContent().contentEquals("#레벨")) {
			new MessageBuilder()
			.setEmbed(new EmbedBuilder()
	            .setTitle("#레벨")
	            .setDescription(
            		"#레벨 (닉네임)을 입력하여 레벨 정보를 확인할 수 있습니다." + "\n" +
    				" " + "\n" +
    				"자세한 정보는 #명령어 메이플 / 메이플스토리 명령어로 확인할 수 있습니다." + "\n" +
    				" " + "\n")
	            .setColor(Color.GREEN))
		    .send(event.getChannel());
		} else if (event.getMessageContent().contentEquals("#유니온")) {
			new MessageBuilder()
			.setEmbed(new EmbedBuilder()
	            .setTitle("#유니온")
	            .setDescription(
            		"#유니온 (닉네임)을 입력하여 유니온 정보를 확인할 수 있습니다." + "\n" +
    				" " + "\n" +
    				"자세한 정보는 #명령어 메이플 / 메이플스토리 명령어로 확인할 수 있습니다." + "\n" +
    				" " + "\n")
	            .setColor(Color.GREEN))
		    .send(event.getChannel());
		} else if (event.getMessageContent().contentEquals("#업적")) {
			new MessageBuilder()
			.setEmbed(new EmbedBuilder()
	            .setTitle("#업적")
	            .setDescription(
            		"#업적 (닉네임)을 입력하여 업적 정보를 확인할 수 있습니다." + "\n" +
    				" " + "\n" +
    				"자세한 정보는 #명령어 메이플 / 메이플스토리 명령어로 확인할 수 있습니다." + "\n" +
    				" " + "\n")
	            .setColor(Color.GREEN))
		    .send(event.getChannel());
		} else if (event.getMessageContent().contentEquals("#무릉")) {
			new MessageBuilder()
			.setEmbed(new EmbedBuilder()
	            .setTitle("#업적")
	            .setDescription(
            		"#무릉 (닉네임)을 입력하여 무릉 정보를 확인할 수 있습니다." + "\n" +
    				" " + "\n" +
    				"자세한 정보는 #명령어 메이플 / 메이플스토리 명령어로 확인할 수 있습니다." + "\n" +
    				" " + "\n")
	            .setColor(Color.GREEN))
		    .send(event.getChannel());
		} else if (event.getMessageContent().contentEquals("#더시드")) {
			new MessageBuilder()
			.setEmbed(new EmbedBuilder()
	            .setTitle("#업적")
	            .setDescription(
            		"#더시드 (닉네임)을 입력하여 더시드 정보를 확인할 수 있습니다." + "\n" +
    				" " + "\n" +
    				"자세한 정보는 #명령어 메이플 / 메이플스토리 명령어로 확인할 수 있습니다." + "\n" +
    				" " + "\n")
	            .setColor(Color.GREEN))
		    .send(event.getChannel());
		} else if (event.getMessageContent().contentEquals("#링크")) {
			new MessageBuilder()
			.setEmbed(new EmbedBuilder()
	            .setTitle("#링크")
	            .setDescription(
            		"#링크 (직업명)을 입력하여 링크&유니온 정보를 확인할 수 있습니다." + "\n" +
    				" " + "\n" +
    				"자세한 정보는 #명령어 메이플 / 메이플스토리 명령어로 확인할 수 있습니다." + "\n" +
    				" " + "\n")
	            .setColor(Color.GREEN))
		    .send(event.getChannel());
		}
					
		// 명령어
		if (event.getMessageContent().contains("#정보 ")) {
			CharName = event.getMessageContent().substring(4, event.getMessageContent().length());
			MapleggImg();
			if (CharName.contains(" ") || CharName.length() == 1 || CharName.matches("^[0-9]*$") || IsName == true) {
				new MessageBuilder()
				.setEmbed(new EmbedBuilder()
			            .setTitle("Warring!")
			            .setDescription(
		            		"캐릭터를 찾을 수 없습니다." + "\n" +
            				" " + "\n")
			            .setColor(Color.RED))
			    .send(event.getChannel());
			} else {
				new MessageBuilder()
				.addAttachment(new File("photo/Maplegg/CharName" + CharCount + ".png"))
				//.addAttachment(new File("/home/ec2-user/SON7BOT/photo/Maplegg/CharName" + CharCount + ".png"))
				.send(event.getChannel());
				
				CharCount++;
			}
			Main.isText = false;
			Main.TextCount++;
		} else if (event.getMessageContent().contains("#레벨 ")) {
			CharName = event.getMessageContent().substring(4, event.getMessageContent().length());
			IsLevel = true;
			MapleInfo();
			if (CharName.contains(" ") || CharName.length() == 1 || IsName == true) {
				new MessageBuilder()
				.setEmbed(new EmbedBuilder()
			            .setTitle("Warring!")
			            .setDescription(
		            		"캐릭터를 찾을 수 없습니다." + "\n" +
            				" " + "\n")
			            .setColor(Color.RED))
			    .send(event.getChannel());
			} else {
				// 레벨 확인용
				MapleLevel = MapleLevel.replace("Lv.", "");
				String LevelStringTemp = MapleLevel.substring(0, MapleLevel.lastIndexOf("("));
				int LevelIntTemp = Integer.valueOf(LevelStringTemp);
				MapleLevel = MapleLevel.replace("(", " (");
				
				// 남은 레벨 계산
				String ExpTempS = MapleExp.replace(",", "");	
				Long ExpTempL = Long.parseLong(ExpTempS);
				
				// 레벨 데이터
				long LevelOldOldOldMax = 11462335230L; // 200
				long LevelOldOldMax = 7764453421743L; // 250
				long LevelOldMax = 82351036260259L; // 275
				long LevelMax = 10100775367634732L; // 300
				
				LevelOldOldOldMax = LevelOldOldOldMax - ExpTempL;
				LevelOldOldMax = LevelOldOldMax - ExpTempL;
				LevelOldMax = LevelOldMax - ExpTempL;
				LevelMax = LevelMax - ExpTempL;
					
				// 3자리마다 , 추가
				String LOldOldOldMax  = String.valueOf(NumberFormat.getInstance().format(LevelOldOldOldMax));
				String LOldOldMax = String.valueOf(NumberFormat.getInstance().format(LevelOldOldMax));
				String LOldMax = String.valueOf(NumberFormat.getInstance().format(LevelOldMax));
				String LMax = String.valueOf(NumberFormat.getInstance().format(LevelMax));
				
				if (CNTemp.contains("리부트")) {
					CNTemp = CNTemp.replace(" - 리부트", "&w=254");	
				}
				
				if (LevelIntTemp <= 199) {
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()
		            .setTitle(CharName)
		            .setDescription(
	            		"직업 : " + MapleJob + "\n" +
	    				"레벨 : " + MapleLevel + "\n" +
	    				"현재 경험치 : " + MapleExp + "\n" +
	    				" " + "\n" +
	    				"200까지 남은 경험치 : " + LOldOldOldMax.replace("-", "") + "\n" +
	    				"250까지 남은 경험치 : " + LOldOldMax + "\n" +
	    				"275까지 남은 경험치 : " + LOldMax + "\n" +
	    				"300까지 남은 경험치 : " + LMax + "\n" +
	    				" " + "\n" +
	    				MapleLeveUrl + CNTemp)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				} else if (LevelIntTemp <= 249) {
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()
		            .setTitle(CharName)
		            .setDescription(
	            		"직업 : " + MapleJob + "\n" +
	    				"레벨 : " + MapleLevel + "\n" +
	    				"현재 경험치 : " + MapleExp + "\n" +
	    				" " + "\n" +
	    				"250까지 남은 경험치 : " + LOldOldMax + "\n" +
	    				"275까지 남은 경험치 : " + LOldMax + "\n" +
	    				"300까지 남은 경험치 : " + LMax + "\n" +
	    				" " + "\n" +
	    				MapleLeveUrl + CNTemp)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				} else if (LevelIntTemp <= 274) { 
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()
		            .setTitle(CharName)
		            .setDescription(
	            		"직업 : " + MapleJob + "\n" +
	    				"레벨 : " + MapleLevel + "\n" +
	    				"현재 경험치 : " + MapleExp + "\n" +
	    				" " + "\n" +
	    				"275까지 남은 경험치 : " + LOldMax + "\n" +
	    				"300까지 남은 경험치 : " + LMax + "\n" +
	    				" " + "\n" +
	    				MapleLeveUrl + CNTemp)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				} else {
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()
		            .setTitle(CharName)
		            .setDescription(
	            		"직업 : " + MapleJob + "\n" +
	    				"레벨 : " + MapleLevel + "\n" +
	    				"현재 경험치 : " + MapleExp + "\n" +
	    				" " + "\n" +
	    				"300까지 남은 경험치 : " + LMax + "\n" +
	    				" " + "\n" +
	    				MapleLeveUrl + CNTemp)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				}
			}
			Main.isText = false;
			Main.TextCount++;
		} else if (event.getMessageContent().contains("#유니온 ")) {
			CharName = event.getMessageContent().substring(5, event.getMessageContent().length());
			IsUnion = true;
			MapleInfo();
			if (CharName.contains(" ") || CharName.length() == 1 || IsName == true) {
				new MessageBuilder()
				.setEmbed(new EmbedBuilder()
			            .setTitle("Warring!")
			            .setDescription(
		            		"캐릭터를 찾을 수 없습니다." + "\n" +
            				" " + "\n")
			            .setColor(Color.RED))
			    .send(event.getChannel());
			} else {
				if (ULevel == null || UGrade == null || UPower == null || UDate == null) {
					UImg = null;
					
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		        		"레벨 : 기록없음\n" +
						"등급 : 기록없음\n" +
						"전투력 : 기록없음\n" +
						"기준일 : 기록없음\n" +
						"일일 코인 획득량 : 기록없음\n" +
						" " + "\n" +
						MapleInfoUrl + CharName)
		            .setThumbnail(UImg)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				} else {
					ULevel = ULevel.replace("Lv.", "");
					UPower = UPower.replace("전투력 ", "");
					UDate = UDate.replace("기준일: ", "");
					int UCoinTemp = Integer.valueOf(UPower.replace(",", ""));
					UCoin = (int) (UCoinTemp * 8.64 / 10000000);
					
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		        		"레벨 : " + ULevel + "\n" +
						"등급 : " + UGrade + "\n" +
						"전투력 : " + UPower + "\n" +
						"기준일 : " + UDate + "\n" +
						"일일 코인 획득량 : " + UCoin + "\n" +
						" " + "\n" +
						MapleInfoUrl + CharName)
		            .setThumbnail(UImg)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				}
			}
			Main.isText = false;
			Main.TextCount++;
		} else if (event.getMessageContent().contains("#업적 ")) {
			CharName = event.getMessageContent().substring(4, event.getMessageContent().length());
			IsAchievement = true;
			MapleInfo();
			if (CharName.contains(" ") || CharName.length() == 1 || IsName == true) {
				new MessageBuilder()
				.setEmbed(new EmbedBuilder()
			            .setTitle("Warring!")
			            .setDescription(
		            		"캐릭터를 찾을 수 없습니다." + "\n" +
            				" " + "\n")
			            .setColor(Color.RED))
			    .send(event.getChannel());
			} else {
				if (ALevel == null || AGrade == null || ADate == null) {
					AImg = null;
					
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		        		"업적점수 : 기록없음\n" +
						"등급 : 기록없음\n" +
						"기준일 : 기록없음\n" +
						" " + "\n" +
						MapleInfoUrl + CharName)
		            .setThumbnail(AImg)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				} else {
					ALevel = ALevel.replace("업적점수 ", "");
					ADate = ADate.replace("기준일: ", "");
					
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		        		"업적점수 : " + ALevel + "\n" +
						"등급 : " + AGrade + "\n" +
						"기준일 : " + ADate + "\n" +
						" " + "\n" +
						MapleInfoUrl + CharName)
		            .setThumbnail(AImg)
		            .setColor(Color.BLUE))
				    .send(event.getChannel());
				}		
			}
			
			Main.isText = false;
			Main.TextCount++;
		} else if (event.getMessageContent().contains("#무릉 ")) {
			CharName = event.getMessageContent().substring(4, event.getMessageContent().length());
			IsMulung = true;
			MapleInfo();
			if (CharName.contains(" ") || CharName.length() == 1 || IsName == true) {
				new MessageBuilder()
				.setEmbed(new EmbedBuilder()
			            .setTitle("Warring!")
			            .setDescription(
		            		"캐릭터를 찾을 수 없습니다." + "\n" +
            				" " + "\n")
			            .setColor(Color.RED))
			    .send(event.getChannel());
			} else {
				if (MGrade == 0 || MTime == null || MLevel == null || MJob == null || MWRank == null || MRank == null || MDate == null) {
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		        		"최고기록 : 기록없음\n" +
						"시간 : 기록없음\n" +
						"레벨 : 기록없음\n" +
						"직업 : 기록없음\n" +
						"월드랭킹 : 기록없음\n" +
						"랭킹 : 기록없음\n" +
						"기준일 : 기록없음\n" +
						" " + "\n" +
						MapleInfoUrl + CharName)
		            .setColor(Color.RED))
				    .send(event.getChannel());
				} else {			
					MLevel = MLevel.replace("Lv.", "");
					MDate = MDate.replace("기준일: ", "");
					
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		            		"최고기록 : " + MGrade + "층" + "\n" +
    						"시간 : " + MTime + "\n" +
    						"레벨 : " + MLevel + "\n" +
    						"직업 : " + MJob + "\n" +
    						"월드랭킹 : " + MWRank + "\n" +
    						"랭킹 : " + MRank + "\n" +
    						"기준일 : " + MDate + "\n" +
    						" " + "\n" +
    						"몬스터 : " + MobData[MGrade] + "\n" +
    						"몬스터 레벨 : " + MobLevel[MGrade] + "\n" +
    						"몬스터 체력 : " + MobHp[MGrade] + " " + "(1/10 감소)" + "\n" +
    						"몬스터 누적 체력 : " + MobAcHp[MGrade] + " " + "(1/10 감소)" + "\n" +
    						"몬스터 방어율 : " + MobDef[MGrade] + "\n" +
    						" " + "\n" +
    						MapleInfoUrl + CharName)
		            .setThumbnail(new File("photo/MuLung/" + MGrade + ".png"))
		            //.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/MuLung/" + MGrade + ".png"))
		            .setColor(Color.RED))
				    .send(event.getChannel());
				}
			}
			
			Main.isText = false;
			Main.TextCount++;
		} else if (event.getMessageContent().contains("#더시드 ")) {
			CharName = event.getMessageContent().substring(5, event.getMessageContent().length());
			IsSeed = true;
			MapleInfo();
			if (CharName.contains(" ") || CharName.length() == 1 || IsName == true) {
				new MessageBuilder()
				.setEmbed(new EmbedBuilder()
			            .setTitle("Warring!")
			            .setDescription(
		            		"캐릭터를 찾을 수 없습니다." + "\n" +
            				" " + "\n")
			            .setColor(Color.RED))
			    .send(event.getChannel());
			} else {
				if (SGrade == null || STime == null || SWRank == null || SRank == null || SDate == null) {
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		        		"최고기록 : 기록없음\n" +
						"시간 : 기록없음\n" +
						"월드랭킹 : 기록없음\n" +
						"랭킹 : 기록없음\n" +
						"기준일 : 기록없음\n" +
						" " + "\n" +
						MapleInfoUrl + CharName)
		            .setColor(Color.PINK))
				    .send(event.getChannel());
				} else {
					SDate = SDate.replace("기준일: ", "");
					
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()	
		            .setTitle(CharName)
		            .setDescription(
		            		"최고기록 : " + SGrade + "\n" +
    						"시간 : " + STime + "\n" +
    						"월드랭킹 : " + SWRank + "\n" +
    						"랭킹 : " + SRank + "\n" +
    						"기준일 : " + SDate + "\n" +
							" " + "\n" +
							MapleInfoUrl + CharName)
		            .setThumbnail(new File("photo/Maple/Seed.png"))
		            //.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/Seed.png"))
		            .setColor(Color.PINK))
				    .send(event.getChannel());
				}
			}
			
			Main.isText = false;
			Main.TextCount++;
		} else if (event.getMessageContent().contains("#링크 ")) {
			JobName = event.getMessageContent().substring(4, event.getMessageContent().length());
			
			switch (JobName) {
				case "모험가 전사" : case "전사": case "모전": case "히어로": case "팔라딘":
					embed = new EmbedBuilder()
					 	.setTitle("모험가 전사")
					 	.setDescription("인빈서블 빌리프")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"현재 HP가 최대 HP의 15% 이하가 되었을 때 자동 발동되어"
							+ "\n"
							+ "3초 동안 1초마다 최대 HP의 20/23/26/29/32/35% 회복"
							+ "\n"
							+ "재발동 대기시간 410/370/330/290/250/210초")
						.addInlineField("유니온 효과", "STR 10/20/40/80/100 증가")
						.setColor(Color.RED)
						.setImage(new File("photo/Maple/Warrior.png"))
						.setThumbnail(new File("photo/Maple/WarriorIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Warrior.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/WarriorIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "다크나이트": case "닼나":
					embed = new EmbedBuilder()
					 	.setTitle("다크나이트")
					 	.setDescription("인빈서블 빌리프")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"현재 HP가 최대 HP의 15% 이하가 되었을 때 자동 발동되어"
							+ "\n"
							+ "3초 동안 1초마다 최대 HP의 20/23/26/29/32/35% 회복"
							+ "\n"
							+ "재발동 대기시간 410/370/330/290/250/210초")
						.addInlineField("유니온 효과", "최대 HP 2/3/4/5/6% 증가")
						.setColor(Color.RED)
						.setImage(new File("photo/Maple/Warrior.png"))
						.setThumbnail(new File("photo/Maple/WarriorIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Warrior.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/WarriorIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "모험가 법사" : case "마법사": case "법사": case "모법": case "아크메이지(썬,콜)": case "썬콜": case "비숍": case "숍":
					embed = new EmbedBuilder()
					 	.setTitle("모험가 마법사")
					 	.setDescription("임피리컬 널리지")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"공격한 적 중 최대 HP가 가장 높은 적의 약점을 15/17/19/21/23/25% 확률로 파악"
							+ "\n"
							+ "파악한 약점은 10초 동안 지속되며 최대 3회까지 중첩"
							+ "\n"
							+ "중첩 당 데미지 1/1/2/2/3/3% 증가"
							+ "\n"
							+ "방어율 무시 1/1/2/2/3/3% 증가")
						.addInlineField("유니온 효과", "INT 10/20/40/80/100 증가")
						.setColor(Color.BLUE)
						.setImage(new File("photo/Maple/Mage.png"))
						.setThumbnail(new File("photo/Maple/MageIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Mage.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/MageIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "아크메이지(불,독)": case "불독":
					embed = new EmbedBuilder()
					 	.setTitle("아크메이지(불,독)")
					 	.setDescription("임피리컬 널리지")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"공격한 적 중 최대 HP가 가장 높은 적의 약점을 15/17/19/21/23/25% 확률로 파악"
							+ "\n"
							+ "파악한 약점은 10초 동안 지속되며 최대 3회까지 중첩"
							+ "\n"
							+ "중첩 당 데미지 1/1/2/2/3/3% 증가"
							+ "\n"
							+ "방어율 무시 1/1/2/2/3/3% 증가")
						.addInlineField("유니온 효과", "최대 MP 2/3/4/5/6% 증가")
						.setColor(Color.BLUE)
						.setImage(new File("photo/Maple/Mage.png"))
						.setThumbnail(new File("photo/Maple/MageIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Mage.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/MageIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "모험가 궁수" : case "궁수": case "모궁": case "보우마스터": case "보마": case "패스파인더": case "패파":
					embed = new EmbedBuilder()
					 	.setTitle("모험가 궁수")
					 	.setDescription("어드벤쳐러 큐리어스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"몬스터 컬렉션 등록 확률 10/15/20/25/30/35% 증가"
							+ "\n"
							+ "크리티컬 확률 3/4/5/7/8/10% 증가")
						.addInlineField("유니온 효과", "DEX 10/20/40/80/100 증가")
						.setColor(Color.GREEN)
						.setImage(new File("photo/Maple/Archer.png"))
						.setThumbnail(new File("photo/Maple/ArcherIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Archer.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/ArcherIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "신궁":
					embed = new EmbedBuilder()
					 	.setTitle("신궁")
					 	.setDescription("어드벤쳐러 큐리어스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"몬스터 컬렉션 등록 확률 10/15/20/25/30/35% 증가"
							+ "\n"
							+ "크리티컬 확률 3/4/5/7/8/10% 증가")
						.addInlineField("유니온 효과", "크리티컬 확률 1/2/3/4/5% 증가")
						.setColor(Color.GREEN)
						.setImage(new File("photo/Maple/Archer.png"))
						.setThumbnail(new File("photo/Maple/ArcherIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Archer.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/ArcherIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "나이트로드": case "나로":
					embed = new EmbedBuilder()
					 	.setTitle("나이트로드")
					 	.setDescription("시프 커닝")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"적에게 상태이상을 적용시키면"
							+ "\n"
							+ "10초 동안 데미지 3/6/9/12/15/18% 증가"
							+ "\n"
							+ "재발동 대기시간 20초")
						.addInlineField("유니온 효과", "크리티컬 확률 1/2/3/4/5% 증가")
						.setColor(Color.MAGENTA)
						.setImage(new File("photo/Maple/Thief.png"))
						.setThumbnail(new File("photo/Maple/ThiefIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Thief.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/ThiefIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "모험가 도적": case "도적": case "모도": case "섀도어": case "섀도": case "듀얼블레이더": case "듀얼블레이드": case "듀블":
					embed = new EmbedBuilder()
					 	.setTitle("모험가 도적")
					 	.setDescription("시프 커닝")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"적에게 상태이상을 적용시키면"
							+ "\n"
							+ "10초 동안 데미지 3/6/9/12/15/18% 증가"
							+ "\n"
							+ "재발동 대기시간 20초")
						.addInlineField("유니온 효과", "LUK 10/20/40/80/100 증가")
						.setColor(Color.MAGENTA)
						.setImage(new File("photo/Maple/Thief.png"))
						.setThumbnail(new File("photo/Maple/ThiefIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Thief.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/ThiefIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "캡틴":
					embed = new EmbedBuilder()
					 	.setTitle("캡틴")
					 	.setDescription("파이렛 블레스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"올스탯 20/30/40/50/60/70 증가" 
							+ "\n" 
							+ "최대 HP, MP 350/525/700/875/1050/1225 증가"
							+ "\n" 
							+ "피격 데미지 5/7/9/11/13/15% 감소")
						.addField("모험가 해적", 
							"스킬을 사용하면 무기, 보조 무기, 아케인 심볼, 펫장비를 제외한"
							+ "\n"
							+ "부위에 장착된 장비의 힘과 민첩이 바뀌어 적용"
							+ "\n"
							+ "재사용 대기시간 5초")
						.addInlineField("유니온 효과", "소환수 지속 시간 4/6/8/10/12% 증가")
						.setColor(Color.GRAY)
						.setImage(new File("photo/Maple/Pirate.png"))
						.setThumbnail(new File("photo/Maple/PirateIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Pirate.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/PirateIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "모험가 해적" : case "해적": case "모해": case "바이퍼": case "캐논슈터": case "캐논": case "캐슈":
					embed = new EmbedBuilder()
					 	.setTitle("모험가 해적")
					 	.setDescription("파이렛 블레스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"올스탯 20/30/40/50/60/70 증가" 
							+ "\n" 
							+ "최대 HP, MP 350/525/700/875/1050/1225 증가"
							+ "\n" 
							+ "피격 데미지 5/7/9/11/13/15% 감소")
						.addField("모험가 해적", 
							"스킬을 사용하면 무기, 보조 무기, 아케인 심볼, 펫장비를 제외한"
							+ "\n"
							+ "부위에 장착된 장비의 힘과 민첩이 바뀌어 적용"
							+ "\n"
							+ "재사용 대기시간 5초")
						.addInlineField("유니온 효과", "STR 10/20/40/80/100 증가")
						.setColor(Color.GRAY)
						.setImage(new File("photo/Maple/Pirate.png"))
						.setThumbnail(new File("photo/Maple/PirateIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Pirate.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/PirateIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "시그너스 기사단": case "시그너스": case "시그": 
				case "소울마스터": case "소마": 
				case "플레임위자드": case "플위": 
				case "윈드브레이커": case "윈브":
				case "나이트워커": case "나워":
				case "스트라이커": case "스커":
					embed = new EmbedBuilder()
					 	.setTitle("시그너스 기사단")
					 	.setDescription("시그너스 블레스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"공격력과 마력 7/9/11/13/15/17/19/21/23/25 증가"
							+ "\n"
							+ "상태이상 내성 2/5/7/10/12/15/17/20/22/25 증가"
							+ "\n"
							+ "모든 속성 내성 2/5/7/10/12/15/17/20/22/25% 증가")
						.addField("유니온 효과 (소울마스터)", "최대 HP 250/500/1000/2000/2500 증가")
						.addField("유니온 효과 (플레임위자드)", "INT 10/20/40/80/100 증가")
						.addField("유니온 효과 (윈드브레이커)", "DEX 10/20/40/80/100 증가")
						.addField("유니온 효과 (나이트워커)", "LUK 10/20/40/80/100 증가")
						.addField("유니온 효과 (스트라이커)", "STR 10/20/40/80/100 증가")
						.setColor(Color.GREEN)
						.setImage(new File("photo/Maple/Cygnus.png"))
						.setThumbnail(new File("photo/Maple/CygnusIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Cygnus.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/CygnusIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "미하일": case "막을게":
					embed = new EmbedBuilder()
					 	.setTitle(JobName)
					 	.setDescription("빛의 수호")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"10/15초 동안 상태 이상 내성 100 증가"
							+ "\n"
							+ "재사용 대기시간 120초")
						.addField("미하일", 
							"15/30초 동안 보호막이 생성, 최대 3번의 피해를 방어"
							+ "\n"
							+ "데미지 15/20% 증가"
							+ "\n"
							+ "단, 최대 HP의 일정 비율로 피해를 입히는 공격에 한해"
							+ "\n"
							+ "피해 10/20% 감소"
							+ "\n"
							+ "재사용 대기시간 180초")
							.addInlineField("유니온 효과", "최대 HP 250/500/1000/2000/2500 증가")
						.setColor(Color.YELLOW)
						.setImage(new File("photo/Maple/Mihile.png"))
						.setThumbnail(new File("photo/Maple/MihileIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Mihile.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/MihileIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "레지스탕스": case "레지": 
				case "블래스터": case "블래": 
				case "배틀메이지": case "배메": 
				case "와일드헌터": case "와헌":
				case "메카닉": case "메카": case "세탁기":
					embed = new EmbedBuilder()
					 	.setTitle("레지스탕스")
					 	.setDescription("스피릿 오브 프리덤")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"부활 시 1/2/3/4/5/6/7/8초 동안 피해를 받지 않음 "
								+ "\n"
								+ "맵 이동시 해제")
						.addField("유니온 효과 (블래스터)", "방어율 무시 1/2/3/5/6% 증가")
						.addField("유니온 효과 (배틀메이지)", "INT 10/20/40/80/100 증가")
						.addField("유니온 효과 (와일드헌터)", "공격 시 20% 확률로 데미지 4/8/12/16/20% 증가")
						.addField("유니온 효과 (메카닉)", "버프 지속 시간 5/10/15/20/25% 증가")
						.setColor(Color.WHITE)
						.setImage(new File("photo/Maple/Resistance.png"))
						.setThumbnail(new File("photo/Maple/ResistanceIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Resistance.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/ResistanceIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "제논":
					embed = new EmbedBuilder()
					 	.setTitle("제논")
					 	.setDescription("룬 퍼시스턴스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "모든 능력치 5/10% 증가")
						.addInlineField("유니온 효과", "STR·DEX·LUK 5/10/20/40/50 증가")
						.setColor(Color.CYAN)
						.setImage(new File("photo/Maple/Xenon.png"))
						.setThumbnail(new File("photo/Maple/XenonIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Xenon.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/XenonIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "데몬슬레이어": case "데몬 슬레이어": case "데몬": case "데슬":
					embed = new EmbedBuilder()
					 	.setTitle("데몬 슬레이어")
					 	.setDescription("데몬스 퓨리")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "보스 몬스터 공격시 데미지 10/15% 증가")
						.addField("데몬슬레이어", "보스 몬스터 공격시 데미지 10/15% 증가 10의 포스 추가 흡수")
						.addInlineField("유니온 효과", "모든 상태 이상 저항 1/2/3/4/5 증가")
						.setColor(Color.RED)
						.setImage(new File("photo/Maple/Demon.png"))
						.setThumbnail(new File("photo/Maple/DemonIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Demon.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/DemonIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "데몬어벤져": case "데몬 어벤져": case "데벤져": case "데벤":
					embed = new EmbedBuilder()
					 	.setTitle("데몬 어벤져")
					 	.setDescription("와일드 레이지")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "데미지 5/10% 증가")
						.addInlineField("유니온 효과", "보스 공격 시 데미지 1/2/3/5/6% 증가")
						.setColor(Color.MAGENTA)
						.setImage(new File("photo/Maple/DemonA.png"))
						.setThumbnail(new File("photo/Maple/DemonAIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/DemonA.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/DemonAIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "아란":
					embed = new EmbedBuilder()
					 	.setTitle("아란")
					 	.setDescription("콤보킬 어드밴티지")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "영구적으로 콤보킬 구슬 경험치 획득량 400/650% 추가 획득")
						.addInlineField("유니온 효과", 
							"적 타격 시 70%의 확률로"
							+ "\n"
							+ "순수 HP의 2/4/6/8/10% 회복"
							+ "\n"
							+ "발동 시 다음 발동 확률이 감소하지만 효과가 2배"
							+ "\n"
							+ "(10초마다 1번)")
						.setColor(Color.LIGHT_GRAY)
						.setImage(new File("photo/Maple/Aran.png"))
						.setThumbnail(new File("photo/Maple/AranIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Aran.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/AranIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "에반":
					embed = new EmbedBuilder()
					 	.setTitle("에반")
					 	.setDescription("룬 퍼시스턴스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "해방된 룬의 힘 지속시간 30/50% 증가")
						.addInlineField("유니온 효과", 
							"적 타격 시 70%의 확률로"
							+ "\n"
							+ "순수 MP의 2/4/6/8/10% 회복"
							+ "\n"
							+ "발동 시 다음 발동 확률이 감소하지만 효과가 2배"
							+ "\n"
							+ "(10초마다 1번)")
						.setColor(Color.YELLOW)
						.setImage(new File("photo/Maple/Evan.png"))
						.setThumbnail(new File("photo/Maple/EvanIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Evan.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/EvanIcon.png"));
					event.getChannel().sendMessage(embed);
					break;				
				case "루미너스": case "루미":
					embed = new EmbedBuilder()
					 	.setTitle("루미너스")
					 	.setDescription("퍼미에이트")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "방어율 무시 10/15% 증가")
						.addInlineField("유니온 효과", "INT 10/20/40/80/100 증가")
						.setColor(Color.WHITE)
						.setImage(new File("photo/Maple/Luminous.png"))
						.setThumbnail(new File("photo/Maple/LuminousIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Luminous.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/LuminousIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "메르세데스": case "메르": case "메세":
					embed = new EmbedBuilder()
					 	.setTitle("메르세데스")
					 	.setDescription("엘프의 축복")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"사용 시 에우렐로 귀환"
							+ "\n"
							+ "재사용 대기시간 600초"
							+ "\n"
							+ "추가효과 : 영구적으로 경험치 10/15% 추가 획득")
						.addInlineField("유니온 효과", 
							"스킬 재사용 대기시간 2/3/4/5/6% 감소"
							+ "\n"
							+ "(1초 미만으로 줄어들지 않음)")
						.setColor(Color.GREEN)
						.setImage(new File("photo/Maple/Mercedes.png"))
						.setThumbnail(new File("photo/Maple/MercedesIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Mercedes.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/MercedesIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "팬텀":
					embed = new EmbedBuilder()
					 	.setTitle("팬텀")
					 	.setDescription("데들리 인스팅트")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "크리티컬 확률 10/15% 증가")
						.addInlineField("유니온 효과", "메소 획득량 1/2/3/4/5% 증가")
						.setColor(Color.BLUE)
						.setImage(new File("photo/Maple/Phantom.png"))
						.setThumbnail(new File("photo/Maple/PhantomIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Phantom.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/PhantomIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "은월":
					embed = new EmbedBuilder()
					 	.setTitle("은월")
					 	.setDescription("구사 일생")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "사망에 이르는 공격을 당할 시, 5/10% 확률로 생존")
						.addInlineField("유니온 효과", "크리티컬 데미지 1/2/3/5/6% 증가")
						.setColor(Color.GRAY)
						.setImage(new File("photo/Maple/Eunwol.png"))
						.setThumbnail(new File("photo/Maple/EunwolIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Eunwol.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/EunwolIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "카이저":
					embed = new EmbedBuilder()
					 	.setTitle("카이저")
					 	.setDescription("아이언 윌")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", "HP 10/15% 증가")
						.addField("카이저", "HP 10% 증가, 모프 게이지 단계당 데미지 2/3% 증가")
						.addInlineField("유니온 효과", "STR 10/20/40/80/100 증가")
						.setColor(Color.RED)
						.setImage(new File("photo/Maple/Kaiser.png"))
						.setThumbnail(new File("photo/Maple/KaiserIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Kaiser.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/KaiserIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "카인":
					embed = new EmbedBuilder()
					 	.setTitle("카인")
					 	.setDescription("프라이어 프리퍼레이션")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"적 8명 처치, 혹은 보스 몬스터에게 5번 공격 적중 시 사전 준비 1번 완료"
							+ "\n"
							+ "사전 준비를 5번 마치면 20초 동안 데미지 9/17% 증가"
							+ "\n"
							+ "재발동 대기시간 40초")
						.addInlineField("유니온 효과", "DEX 10/20/40/80/100 증가")
						.setColor(Color.BLACK)
						.setImage(new File("photo/Maple/Kain.png"))
						.setThumbnail(new File("photo/Maple/KainIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Kain.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/KainIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "카데나":
					embed = new EmbedBuilder()
					 	.setTitle("카데나")
					 	.setDescription("인텐시브 인썰트")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"캐릭터보다 레벨이 낮은 몬스터 공격 시 데미지 3/6% 증가"
							+ "\n"
							+ "상태 이상에 걸린 몬스터 공격 시 데미지 3/6% 증가")
						.addInlineField("유니온 효과", "LUK 10/20/40/80/100 증가")
						.setColor(Color.RED)
						.setImage(new File("photo/Maple/Cadena.png"))
						.setThumbnail(new File("photo/Maple/CadenaIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Cadena.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/CadenaIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "엔젤릭버스터": case "엔버":
					embed = new EmbedBuilder()
					 	.setTitle("엔젤릭버스터")
					 	.setDescription("소울 컨트랙트")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"발동 시 10초 동안 데미지 30/45% 증가"
								+ "\n"
								+ "재사용 대기시간 90초")
						.addField("엔젤릭버스터", 
								"발동 시 10초 동안 스킬 데미지 60/90% 증가"
								+ "\n"
								+ "재사용 대기시간 90초"
								+ "\n"
								+ "링크 스킬로 사용 시 효과가 절반으로 감소")
						.addInlineField("유니온 효과", "DEX 10/20/40/80/100 증가")
						.setColor(Color.PINK)
						.setImage(new File("photo/Maple/Angelicbuster.png"))
						.setThumbnail(new File("photo/Maple/AngelicbusterIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Angelicbuster.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/AngelicbusterIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "아델":
					embed = new EmbedBuilder()
					 	.setTitle("아델")
					 	.setDescription("노블레스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"같은 맵에 있는 자신을 포함한 파티원 1명 당 데미지 1/2% 증가"
								+ "\n"
								+ "최대 4/8%까지 증가"
								+ "\n"
								+ "파티를 하지 않았을 때는 자신만 파티한 것으로 취급"
								+ "\n"
								+ "보스 몬스터 공격 시 데미지 2/4% 증가")
						.addInlineField("유니온 효과", "STR 10/20/40/80/100 증가")
						.setColor(Color.WHITE)
						.setImage(new File("photo/Maple/Adele.png"))
						.setThumbnail(new File("photo/Maple/AdeleIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Adele.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/AdeleIcon.png"));
						event.getChannel().sendMessage(embed);
					break;
				case "일리움":
					embed = new EmbedBuilder()
					 	.setTitle("일리움")
					 	.setDescription("전투의 흐름")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"일정 거리 이동 시 발동되며 최대 6회 중첩가능"
								+ "\n"
								+ "지속시간 5초"
								+ "\n"
								+ "각 중첩당 데미지 1/2% 증가")
						.addInlineField("유니온 효과", "INT 10/20/40/80/100 증가")
						.setColor(Color.BLUE)
						.setImage(new File("photo/Maple/Illium.png"))
						.setThumbnail(new File("photo/Maple/IlliumIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Illium.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/IlliumIcon.png"));
						event.getChannel().sendMessage(embed);
					break;
				case "칼리":
					embed = new EmbedBuilder()
					 	.setTitle("칼리")
					 	.setDescription("이네이트 기프트")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
							"데미지 3/5% 증가"
							+ "\n"
							+ "공격 시 100% 확률로 5초 동안 매초 최대 HP/MP의 1/2% 회복"
							+ "\n"
							+ "재발동 대기시간 30초")
						.addInlineField("유니온 효과", "LUK 10/20/40/80/100 증가")
						.setColor(Color.RED)
						.setImage(new File("photo/Maple/Khali.png"))
						.setThumbnail(new File("photo/Maple/KhaliIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Khali.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/KhaliIcon.png"));
					event.getChannel().sendMessage(embed);
					break;
				case "아크":
					embed = new EmbedBuilder()
					 	.setTitle("아크")
					 	.setDescription("무아")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"전투 상태가 5초 지속되면 발동되며 최대 5회 중첩 가능"
								+ "\n"
								+ "지속시간 5초"
								+ "\n"
								+ "발동 시 데미지 1% 증가"
								+ "\n"
								+ "각 중첩당 데미지 1/2% 증가")
						.addInlineField("유니온 효과", "STR 10/20/40/80/100 증가")
						.setColor(Color.RED)
						.setImage(new File("photo/Maple/Ark.png"))
						.setThumbnail(new File("photo/Maple/ArkIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Ark.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/ArkIcon.png"));
						event.getChannel().sendMessage(embed);
					break;
				case "라라":
					embed = new EmbedBuilder()
					 	.setTitle("라라")
					 	.setDescription("자연의 벗")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"데미지 3/5% 증가"
								+ "\n"
								+ "일반 몬스터 20명 처치 시 자연의 도움 발동"
								+ "\n"
								+ "자연의 도움 발동 시 30초 동안"
								+ "\n"
								+ "일반 몬스터 공격 시 데미지 7/11% 증가"
								+ "\n"
								+ "재발동 대기시간 30초")
						.addInlineField("유니온 효과", "INT 10/20/40/80/100 증가")
						.setColor(Color.YELLOW)
						.setImage(new File("photo/Maple/Lara.png"))
						.setThumbnail(new File("photo/Maple/LaraIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Lara.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/LaraIcon.png"));
						event.getChannel().sendMessage(embed);
					break;
				case "호영":
					embed = new EmbedBuilder()
					 	.setTitle("호영")
					 	.setDescription("자신감")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"방어율 무시 5/10% 추가"
								+ "\n"
								+ "HP가 100%인 몬스터 공격 시 데미지 9/14% 증가")
						.addInlineField("유니온 효과", "LUK 10/20/40/80/100 증가")
						.setColor(Color.WHITE)
						.setImage(new File("photo/Maple/Hoyoung.png"))
						.setThumbnail(new File("photo/Maple/HoyoungIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Hoyoung.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/HoyoungIcon.png"));
						event.getChannel().sendMessage(embed);
					break;
				case "제로":
					embed = new EmbedBuilder()
					 	.setTitle("제로")
					 	.setDescription("노블레스")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"받는 데미지 3/6/9/12/15% 감소"
								+ "\n"
								+ "공격 시 대상의 방어율 2/4/6/8/10% 무시")
						.addInlineField("유니온 효과", "경험치 획득량 4/6/8/10/12% 증가")
						.setColor(Color.CYAN)
						.setImage(new File("photo/Maple/Zero.png"))
						.setThumbnail(new File("photo/Maple/ZeroIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Zero.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/ZeroIcon.png"));
						event.getChannel().sendMessage(embed);
					break;
				case "키네시스": case "키네":
					embed = new EmbedBuilder()
					 	.setTitle("키네시스")
					 	.setDescription("판단")
						.setAuthor("메이플스토리 링크&유니온 효과")
						.addField("링크 효과", 
								"크리티컬 데미지 2/4% 증가")
						.addInlineField("유니온 효과", "INT 10/20/40/80/100 증가")
						.setColor(Color.BLACK)
						.setImage(new File("photo/Maple/Kinesis.png"))
						.setThumbnail(new File("photo/Maple/KinesisIcon.png"));
						//.setImage(new File("/home/ec2-user/SON7BOT/photo/Maple/Kinesis.png"))
						//.setThumbnail(new File("/home/ec2-user/SON7BOT/photo/Maple/KinesisIcon.png"));
						event.getChannel().sendMessage(embed);
					break;
				default:
					new MessageBuilder()
					.setEmbed(new EmbedBuilder()
			            .setTitle("#링크")
			            .setDescription(
		            		"#링크 (직업명)을 입력하여 링크&유니온 정보를 확인할 수 있습니다." + "\n" +
	        				"상세 정보 확인을 위해서는 (직업명)을 정확히 입력해야 합니다." + "\n" +
	        				"ex) 모험가 전사 X > 히어로 O" + "\n" +
	        				" " + "\n" +
	        				"직업명은 줄여서도 입력할 수 있습니다." + "\n" +
	        				"ex) 모험가 전사, 법사, 모도, 듀블" + "\n" +
	        				" " + "\n"
			            )
			            .setColor(Color.GREEN))
				    .send(event.getChannel());
			}
			
			Main.isText = false;
			Main.TextCount++;
		}
	}
}