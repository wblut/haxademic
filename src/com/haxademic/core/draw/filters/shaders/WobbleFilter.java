package com.haxademic.core.draw.filters.shaders;

import processing.core.PApplet;

public class WobbleFilter
extends BaseFilter {

	public static WobbleFilter instance;
	
	public WobbleFilter(PApplet p) {
		super(p, "shaders/filters/wobble.glsl");
		setSpeed(1f);
		setStrength(0.001f);
		setSize(100f);
	}
	
	public static WobbleFilter instance(PApplet p) {
		if(instance != null) return instance;
		instance = new WobbleFilter(p);
		return instance;
	}
	
	public void setSpeed(float speed) {
		shader.set("speed", speed);
	}
	
	public void setStrength(float strength) {
		shader.set("strength", strength);
	}
	
	public void setSize(float size) {
		shader.set("size", size);
	}
	
}
