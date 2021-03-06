package com.haxademic.sketch.test;

import com.haxademic.core.app.AppSettings;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.ElasticFloat;

import controlP5.ControlP5;

public class ElasticFloatTest
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	public float fric = 0.5f;
	public float accel = 0.5f;
	protected ElasticFloat _elasticX = new ElasticFloat(0, fric, accel);
	protected ElasticFloat _elasticY = new ElasticFloat(0, fric, accel);
	protected ControlP5 _cp5;

	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.FPS, "60" );
	}


	public void setup() {
		super.setup();	
		p.smooth( OpenGLUtil.SMOOTH_HIGH );

		_cp5 = new ControlP5(this);
		_cp5.addSlider("fric").setPosition(20,60).setWidth(200).setRange(0,1);
		_cp5.addSlider("accel").setPosition(20,100).setWidth(200).setRange(0,1);
	}

	public void drawApp() {
		background(0);
		
		_elasticX.setFriction(fric);
		_elasticY.setFriction(fric);
		_elasticX.setAccel(accel);
		_elasticY.setAccel(accel);
		
		_elasticX.setTarget(p.mouseX);
		_elasticY.setTarget(p.mouseY);
		
		_elasticX.update();
		_elasticY.update();
		
		DrawUtil.setDrawCenter(p);
		p.fill(255);
		p.ellipse(_elasticX.val(), _elasticY.val(), 40, 40);
	}

}
