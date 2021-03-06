package com.haxademic.sketch.render.ello;

import java.util.ArrayList;

import com.haxademic.core.app.AppSettings;
import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.image.AnimatedGifEncoder;
import com.haxademic.core.draw.image.ImageUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.file.FileUtil;
import com.haxademic.core.math.MathUtil;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

public class GifRenderEllo018ElloPacking
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	AnimatedGifEncoder encoder;
	PShape _logo;
	PShape _logoInverse;
	PImage _bread;
	float _frames = 300;
	
	PGraphics _logoOffscreen;
	ArrayList<GrowEllo> _particles;
	
	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.WIDTH, "640" );
		p.appConfig.setProperty( AppSettings.HEIGHT, "640" );

		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE, "false" );
		p.appConfig.setProperty( AppSettings.RENDERING_GIF, "false" );
		p.appConfig.setProperty( AppSettings.RENDERING_GIF_FRAMERATE, "40" );
		p.appConfig.setProperty( AppSettings.RENDERING_GIF_QUALITY, "1" );
		p.appConfig.setProperty( AppSettings.RENDERING_GIF_START_FRAME, "2" );
		p.appConfig.setProperty( AppSettings.RENDERING_GIF_STOP_FRAME, ""+Math.round(_frames+1) );
	}
	
	public void setup() {
		super.setup();
		p.smooth(OpenGLUtil.SMOOTH_HIGH);
		_logo = p.loadShape(FileUtil.getHaxademicDataPath()+"svg/ello.svg");
		_logoInverse = p.loadShape(FileUtil.getHaxademicDataPath()+"svg/ello-inverse.svg");
		_bread = p.loadImage(FileUtil.getHaxademicDataPath()+"images/bread.png");
		
		// build off-screen logo image for processing
		_logoOffscreen = p.createGraphics(p.width, p.height);
		_logoOffscreen.beginDraw();
//		_logoOffscreen.background(100,255,100);
		DrawUtil.setDrawCorner(_logoOffscreen);
		float elloSize = p.width * 0.9f;
		_logoOffscreen.shape(_logo, p.width/2, p.height/2, elloSize, elloSize);
		_logoOffscreen.endDraw();
		
		// build particles
		_particles = new ArrayList<GrowEllo>();
	}

	public void drawApp() {
		p.background(255);
		p.noStroke();
				
		// Ello logo
//		DrawUtil.setDrawCorner(p);
//		DrawUtil.setPImageAlpha(p, 0.3f);
//		p.image(_logoOffscreen, 0, 0);
//		DrawUtil.setPImageAlpha(p, 1);
		
		// add particles
		if(_particles.size() < 2000 && p.frameCount <= 250) {
			_particles.add(new GrowEllo());
			_particles.add(new GrowEllo());
			_particles.add(new GrowEllo());
			_particles.add(new GrowEllo());
			_particles.add(new GrowEllo());
			_particles.add(new GrowEllo());
			_particles.add(new GrowEllo());
		}
		P.println(p.frameCount);
		
		// draw particles
		for (int i = 0; i < _particles.size(); i++) {
			_particles.get(i).update();
		}
		
		// clean up small particles
		for (int i = _particles.size() - 1; i > 0; i--) {
			if(_particles.get(i).active() == false && _particles.get(i).size() <= 0) {
				_particles.remove(i);
			}
		}
	}
	
	public class GrowEllo {
		
		float _x;
		float _y;
		float _size;
		boolean _growing;
		boolean _active;
		
		public GrowEllo() {
			_size = 0;
			_active = true;
			_growing = true;
			_x = p.random(0,p.width);
			_y = p.random(0,p.height);
			int attempts = 0;
			while(ImageUtil.getPixelColor(_logoOffscreen, (int)_x, (int)_y) == ImageUtil.EMPTY_INT || isCloseToAnother() == true) {
				_x = p.random(0,p.width);
				_y = p.random(0,p.height);
				attempts++;
				if(attempts > 2000) {
					_active = false;
					_growing = false;
					break;
				}
			}
		}
		
		public float x() { return _x; }
		public float y() { return _y; }
		public float size() { return _size; }
		public float radius() { return _size/2f; }
		public boolean active() { return _active; }
		public boolean growing() { return _growing; }
		
		public void update() {
			// check for collisions
			for (int i = 0; i < _particles.size(); i++) {
				if(_particles.get(i) != this && _particles.get(i).isTouching(this) == true) {
					_growing = false;
				}
			}
			// check for collision with whitespace
			for(float i=0; i < P.TWO_PI; i += P.TWO_PI/36f) {
				float checkX = _x + P.sin(i) * radius();
				float checkY = _y + P.cos(i) * radius();
				if(ImageUtil.getPixelColor(_logoOffscreen, (int)checkX, (int)checkY) == ImageUtil.EMPTY_INT) {
					_growing = false;
				}
			}

			if(_growing == false && radius() < 3) _active = false;
			if(p.frameCount > 250) {
				_active = false;
				_growing = false;
			}
			if(_growing == true) _size += 1;
			if(_growing == false && _active == false && _size > 0) _size -= 1;
			
			
			DrawUtil.setDrawCorner(p);
			p.pushMatrix();
			p.translate(_x, _y);
			p.shape(_logo, 0, 0, _size, _size);
			p.popMatrix();
		}
		
		public boolean isTouching(GrowEllo otherParticle) {
			if(MathUtil.getDistance(_x, _y, otherParticle.x(), otherParticle.y()) < (radius() + otherParticle.radius())) 
				return true;
			else
				return false;
		}
		
		public boolean isCloseToAnother() {
			for (int i = 0; i < _particles.size(); i++) {
				if(_particles.get(i) != this && _particles.get(i).isClose(this) == true) {
					return true;
				}
			}
			return false;
		}
		
		public boolean isClose(GrowEllo otherParticle) {
			if(MathUtil.getDistance(_x, _y, otherParticle.x(), otherParticle.y()) - 3 < (radius() + otherParticle.radius())) 
				return true;
			else
				return false;
		}
	}
}



