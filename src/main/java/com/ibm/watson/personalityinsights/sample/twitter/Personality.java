package com.ibm.watson.personalityinsights.sample.twitter;

public class Personality {
	
	public long Openness;
	public long Conscientiousness;
	public long Extraversion;
	public long Agreeableness;
	public long Neuroticism;
	public long id;
	
	
	public Personality(long openness, long conscientiousness, long extraversion, long agreeableness, long neuroticism, long pid) {
		super();
		Openness = openness;
		Conscientiousness = conscientiousness;
		Extraversion = extraversion;
		Agreeableness = agreeableness;
		Neuroticism = neuroticism;
		id=pid;
	}


	public long getOpenness() {
		return Openness;
	}


	public void setOpenness(long openness) {
		Openness = openness;
	}


	public long getConscientiousness() {
		return Conscientiousness;
	}


	public void setConscientiousness(long conscientiousness) {
		Conscientiousness = conscientiousness;
	}


	public long getExtraversion() {
		return Extraversion;
	}


	public void setExtraversion(long extraversion) {
		Extraversion = extraversion;
	}


	public long getAgreeableness() {
		return Agreeableness;
	}


	public void setAgreeableness(long agreeableness) {
		Agreeableness = agreeableness;
	}


	public long getNeuroticism() {
		return Neuroticism;
	}


	public void setNeuroticism(long neuroticism) {
		Neuroticism = neuroticism;
	}
	
	public long getid() {
		return id;
	}


	public void setid(long id) {
		this.id = id;
	}
	
	
	
}
