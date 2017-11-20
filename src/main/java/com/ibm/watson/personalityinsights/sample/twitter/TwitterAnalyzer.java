package com.ibm.watson.personalityinsights.sample.twitter;

import java.io.BufferedReader;

import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import javax.json.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import twitter4j.Status;

public class TwitterAnalyzer {

	public static void main(String[] args) throws Exception {
		//System.out.println("ingrese ususario de twitter para analizar.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String handle = args.length < 1 ? "jschoudt" : args[0];
		System.out.println("1. Aprender");
		System.out.println("2. Predecir");
		String opcion = br.readLine();
		System.out.println("Ingrese usuario de twitter");
		String lista = br.readLine();
		System.out.println(lista + " recibida, trabajando...");
		String[] listUser = lista.split(",");
		int profesion=0;
		if (opcion.equals("1")) {
			profesion = Integer.parseInt(br.readLine());
		}
		

		Properties props = new Properties();
		props.load(FileUtils.openInputStream(new File("twittersample.properties")));

		Twitter4JHelper twitterHelper = new Twitter4JHelper(props);
		PersonalityInsightsHelper piHelper = new PersonalityInsightsHelper(props);

		HashSet<String> langs = new HashSet<String>();
		langs.add("en");
		langs.add("es");

		try {
			for (int i = 0; i < listUser.length; i++) {

				//System.out.println("trabajando en: " + listUser[i]);

				List<Status> tweets = twitterHelper.getTweets(listUser[i], langs, 200);
				String contentItemsJson = twitterHelper.convertTweetsToPIContentItems(tweets);
				String profile = piHelper.getProfileJSON(contentItemsJson, false);
				//System.out.println(profile);
				//String profile = piHelper.getProfileCSV(contentItemsJson, false);
				System.out.println("perfil de "+listUser[i]+profile);
				JsonReader jreader = Json.createReader(new StringReader(profile));
				JsonObject obj = jreader.readObject();
				JsonArray obj2 = obj.getJsonObject("tree")
						.getJsonArray("children");
				JsonArray obj3 = obj2.getJsonObject(0).getJsonArray("children")
						.getJsonObject(0).getJsonArray("children");
				String[] ListNames = {"Openess", "Conscientiousness", "Extraversion", "Agreeableness", "Neuroticism"};
				String url =imprimirOCEAN(obj3,profesion);
				if (opcion.equals("1")) {
					postToIA(url);
				}
				else
				{
					predictFromIA(obj3);
				}
				
				//
				ArrayList<String> big5 = new ArrayList<String>();
				for (int j = 0; j < 5; j++) {
					//big5.add((ListNames[j]+": " +obj3.getJsonObject(j).getJsonNumber("percentage").toString()+ "\n"));
				}
				//Collections.sort(big5);
				//System.out.println("Usuario: " +listUser[i]);
				//System.out.println("--------------------");

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Usuario no encontrado.");
		}
		
		//System.out.println(profile);
	}

	public static String imprimirOCEAN(JsonArray jsonArray,int prof) {
		String r = "";
		for (int i = 0; i < 5; i++) {
			r +="/"+jsonArray.getJsonObject(i).getJsonNumber("percentage").toString();
		}
		String url = r+"/"+prof;
		System.out.println(url);
		return url;
	}

	public static void postToIA(String url) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource webResource = client.resource(UriBuilder.fromUri("http://172.24.42.22:8000/input"+url).build());
		MultivaluedMap formData = new MultivaluedMapImpl();
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
		System.out.println("Response"  + response.getEntity(String.class));
	}
	
	public static void predictFromIA(JsonArray jsonArray) {
		
		String r = "";
		for (int i = 0; i < 5; i++) {
			r +="/"+jsonArray.getJsonObject(i).getJsonNumber("percentage").toString();
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource webResource = client.resource(UriBuilder.fromUri("http://172.24.42.22:8000/predict"+r).build());
		MultivaluedMap formData = new MultivaluedMapImpl();
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
		System.out.println("Response"  + response.getEntity(String.class));
	}

}
