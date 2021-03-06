package com.haxademic.core.draw.util;

import com.haxademic.core.app.P;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.opengl.Texture;

public class DrawUtil {
	/**
	 * Clears all drawing properties so we may always have the same starting
	 * point when drawing across classes.
	 * 
	 * @param p		Processing Applet for reference to p5 core
	 */
	public static void resetGlobalProps( PApplet p ) {
		// p.resetMatrix();
		p.colorMode( P.RGB, 255, 255, 255, 255 );
		p.fill( 0, 255, 0, 255 );
		p.stroke( 0, 255, 0, 255 );
		p.strokeWeight( 1 );
		p.camera();
		setDrawCenter(p);
	}
	public static void resetGlobalProps(PGraphics p) {
		// p.resetMatrix();
		p.colorMode( P.RGB, 255, 255, 255, 255 );
		p.fill( 0, 255, 0, 255 );
		p.stroke( 0, 255, 0, 255 );
		p.strokeWeight( 1 );
		p.camera();
		setDrawCenter(p);
	}

	public static void setCenter( PApplet p ) {
		// p.resetMatrix();
		p.translate( 0, 0, 0 );
	}
	public static void setCenter(PGraphics p) {
		// p.resetMatrix();
		p.translate( 0, 0, 0 );
	}

	public static void setCenterScreen( PApplet p ) {
		// p.resetMatrix();
		p.translate( p.width/2, p.height/2, 0 );
	}
	public static void setCenterScreen(PGraphics p) {
		// p.resetMatrix();
		p.translate( p.width/2, p.height/2, 0 );
	}

	public static void setTopLeft( PApplet p ) {
		// p.resetMatrix();
		p.translate( -p.width/2, -p.height/2, 0 );
	}
	public static void setTopLeft( PGraphics p ) {
		// p.resetMatrix();
		p.translate( -p.width/2, -p.height/2, 0 );
	}

	public static void setBasicLights( PApplet p ) {
		// setup lighting props
		p.shininess(500); 
		p.lights();
		p.ambientLight(0.2f,0.2f,0.2f, 0, 0, 6000);
		p.ambientLight(0.2f,0.2f,0.2f, 0, 0, -6000);
	}
	public static void setBasicLights( PGraphics p ) {
		// setup lighting props
		p.shininess(500); 
		p.lights();
		p.ambientLight(0.2f,0.2f,0.2f, 0, 0, 6000);
		p.ambientLight(0.2f,0.2f,0.2f, 0, 0, -6000);
	}

	public static void setBetterLights( PApplet p ) {
		setBetterLights(p.g);
	}
	public static void setBetterLights( PGraphics p ) {
		// setup lighting props
		p.ambient(127);
		p.lightSpecular(230, 230, 230); 
		p.directionalLight(200, 200, 200, -0.0f, -0.0f, 1); 
		p.directionalLight(200, 200, 200, 0.0f, 0.0f, -1); 
		p.specular(p.color(200)); 
		p.shininess(5.0f); 
	}
	
	
	public static void setDrawCorner( PApplet p ) { 
		setDrawCorner(p.g);
	}
	public static void setDrawCorner( PGraphics p ) {
		p.imageMode( PConstants.CORNER );
		p.rectMode( PConstants.CORNER );
		p.ellipseMode( PConstants.CORNER );
		p.shapeMode( PConstants.CORNER );
	}
	
	public static void setDrawCenter( PApplet p ) {
		setDrawCenter(p.g);
	}
	public static void setDrawCenter( PGraphics p ) {
		p.imageMode( PConstants.CENTER );
		p.rectMode( PConstants.CENTER );
		p.ellipseMode( PConstants.CENTER );
		p.shapeMode( PConstants.CENTER );
	}
	
	public static void setColorForPImage( PApplet p ) {
		setColorForPImage(p.g);
	}
	public static void setColorForPImage( PGraphics p ) {
		p.fill( 255, 255, 255, 255 );
	}
	
	public static void setPImageAlpha( PApplet p, float alpha ) {
		setPImageAlpha(p.g, alpha);
	};
	public static void setPImageAlpha( PGraphics p, float alpha ) {
		p.tint( 255, alpha * 255 );
	}
	
	public static void resetPImageAlpha( PApplet p ) {
		resetPImageAlpha(p.g);
	};
	public static void resetPImageAlpha( PGraphics p ) {
		p.tint( 255 );
	}
	
	public static void setDrawFlat2d( PApplet p, boolean is2d ) {
		setDrawFlat2d(p.g, is2d);
	};
	public static void setDrawFlat2d( PGraphics p, boolean is2d ) {
		if( is2d ) {
			p.hint( P.DISABLE_DEPTH_TEST );
		} else {
			p.hint( P.ENABLE_DEPTH_TEST );
		}
	}
	
	public static void setTextureRepeat( PApplet p, boolean doesRepeat ) {
		setTextureRepeat(p.g, doesRepeat);
	};
	public static void setTextureRepeat(PGraphics pg, boolean doesRepeat) {
		if( doesRepeat == true ) 
			pg.textureWrap(Texture.REPEAT);
		else 
			pg.textureWrap(Texture.CLAMP);
	}
	
	public static void fadeInOut(PGraphics pg, int color, int startFrame, int stopFrame, int transitionFrames) {
		int frames = stopFrame - startFrame;
		DrawUtil.setDrawCorner(pg);
		if(P.p.frameCount <= startFrame + transitionFrames) {
			pg.fill(color, P.map(P.p.frameCount, 1f, transitionFrames, 255f, 0));
			pg.rect(0,0,pg.width, pg.height);
		} else if(P.p.frameCount >= frames - transitionFrames) {
			pg.fill(color, P.map(P.p.frameCount, frames - transitionFrames, frames, 0, 255f));
			pg.rect(0, 0, pg.width, pg.height);
		} 
	}
	
	public static void feedback(PGraphics pg, int color, float colorFade, float feedbackDistance) {
		DrawUtil.setDrawCorner(pg);
		DrawUtil.setDrawFlat2d(pg, true);
		pg.copy(
			pg, 
			0, 
			0, 
			pg.width, 
			pg.height, 
			P.round(-feedbackDistance/2f), 
			P.round(-feedbackDistance/2f), 
			P.round(pg.width + feedbackDistance), 
			P.round(pg.height + feedbackDistance)
		);
		pg.fill(color, colorFade * 255f);
		pg.noStroke();
		pg.rect(0, 0, pg.width, pg.height);
		DrawUtil.setDrawFlat2d(pg, false);
	}
	
	// from: http://p5art.tumblr.com/post/144205983628/a-small-transparency-tip
	public static void fadeToBlack(PGraphics pg, float blackVal) {
		pg.blendMode(P.SUBTRACT);
		pg.fill(blackVal);
		pg.rect(0, 0, pg.width * 3, pg.height * 3);
		pg.blendMode(P.BLEND);
	}
	
	public static void zoomReTexture(PGraphics pg, float amount) {
		float w = (float) pg.width;
		float h = (float) pg.height;
		float newW = w * amount;
		float newH = h * amount;
		pg.copy(
				(int) (w * 0.5f - newW * 0.5f), 
				(int) (h * 0.5f - newH * 0.5f),
				(int) newW, 
				(int) newH, 
				0, 0, pg.width, pg.height);
	}
	
	public static void rotateRedraw(PGraphics pg, float radians) {
		DrawUtil.setDrawCenter(pg);
		pg.beginDraw();
		pg.pushMatrix();
		DrawUtil.setCenterScreen(pg);
		pg.rotate(radians);
		pg.image(pg, 0, 0);
		pg.popMatrix();
		pg.endDraw();
		DrawUtil.setDrawCorner(pg);
	}
	
	public static void drawTestPattern(PGraphics pg) {
		pg.beginDraw();
		pg.noStroke();
		
		for( int x=0; x < pg.width; x+= 50) {
			for( int y=0; y < pg.height; y+= 50) {
				if( ( x % 100 == 0 && y % 100 == 0 ) || ( x % 100 == 50 && y % 100 == 50 ) ) {
					pg.fill(0);
				} else {
					pg.fill(255);
				}
				pg.rect(x,y,50,50);
			}
		}
		pg.endDraw();
	}


}
