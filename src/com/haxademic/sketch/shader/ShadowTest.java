package com.haxademic.sketch.shader;

import com.haxademic.core.app.AppSettings;
import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.filters.shaders.BlurProcessingFilter;
import com.haxademic.core.draw.filters.shaders.GlowFilter;
import com.haxademic.core.draw.image.ImageUtil;
import com.haxademic.core.draw.shapes.Gradients;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.file.FileUtil;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;

public class ShadowTest
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }
	
	protected PGraphics image;
	protected PGraphics imageShadow;
	protected PShader shadowFilter;
	protected int gradientTop = ColorUtil.colorFromHex("#73909E");
	protected int gradientBottom = ColorUtil.colorFromHex("#ffffff");
	int frames = 200;

	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.WIDTH, 640 );
		p.appConfig.setProperty( AppSettings.HEIGHT, 640 );
		p.appConfig.setProperty( AppSettings.FULLSCREEN, false );
		p.appConfig.setProperty( AppSettings.SMOOTHING, AppSettings.SMOOTH_HIGH );
		p.appConfig.setProperty( AppSettings.RETINA, false );
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE, false );
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE_START_FRAME, 1 );
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE_STOP_FRAME, frames );
	}

	public void setup() {
		super.setup();
		
		// load image into padded buffer
		PImage imageOrig = p.loadImage(FileUtil.getFile("images/cacheflowe-logotype-new.png"));
		image = ImageUtil.imageToGraphicsWithPadding(imageOrig, 0.8f);
		
		// copy buffer and turn it into a shadow
		GlowFilter.instance(p).setGlowColor(0f, 0f, 0f, 0.2f);
		GlowFilter.instance(p).setRadialSamples(64f);
		GlowFilter.instance(p).setSize(10f);
		imageShadow = GlowFilter.instance(p).getShadowBuffer(image, 6);
	}
	
	public void drawApp() {
		float progress = (float) (p.frameCount % frames) / (float) frames;
		
		// draw gradient
		p.pushMatrix();
		DrawUtil.setDrawCenter(p);
		p.translate(p.width/2, p.height/2);
		p.rotate(P.HALF_PI);
		Gradients.linear(p, p.width * 1.1f, p.width * 1.1f, gradientTop, gradientBottom);
		p.popMatrix();

		// draw image & shadow
		float imageScale = ((float) p.height / (float) image.height) * 0.82f;
		DrawUtil.setDrawCenter(p);
		p.pushMatrix();
		p.translate(p.width/2, p.height/2);
		float shadowScale = 0.975f + 0.025f * P.sin(progress * P.TWO_PI);
		p.image(imageShadow, 6f * P.cos(P.PI + progress * P.TWO_PI), 15 + 1f * P.sin(P.PI + progress * P.TWO_PI), image.width * imageScale * shadowScale, image.height * imageScale * shadowScale);
		p.image(image, 0, 0, image.width * imageScale, image.height * imageScale);
		p.popMatrix();
	}
	
}
