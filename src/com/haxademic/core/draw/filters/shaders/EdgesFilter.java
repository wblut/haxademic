package com.haxademic.core.draw.filters.shaders;

import processing.core.PApplet;

public class EdgesFilter
extends BaseFilter {

	public static EdgesFilter instance;
	
	public EdgesFilter(PApplet p) {
		super(p, "shaders/filters/edges.glsl");
	}
	
	public static EdgesFilter instance(PApplet p) {
		if(instance != null) return instance;
		instance = new EdgesFilter(p);
		return instance;
	}

}
