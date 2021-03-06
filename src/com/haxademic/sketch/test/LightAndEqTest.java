package com.haxademic.sketch.test;

import com.haxademic.core.app.AppSettings;
import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.MathUtil;

public class LightAndEqTest
extends PAppletHax {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected float angle;

	protected void overridePropsFile() {
		p.appConfig.setProperty( AppSettings.WIDTH, "1280" );
		p.appConfig.setProperty( AppSettings.HEIGHT, "1280" );
		p.appConfig.setProperty( AppSettings.RENDERING_MOVIE, "false" );
		p.appConfig.setProperty( "force_foreground", "true" );
		p.appConfig.setProperty( "force_foreground", "true" );
	}

	public void setup() {
		super.setup();
		p.smooth(OpenGLUtil.SMOOTH_HIGH);
	}

	public void drawApp() {
		p.background(0);

//		p.pointLight(0, 255, 255, 0, p.mouseY, 500);
//		p.pointLight(255, 255, 0, p.width, p.mouseY, 500);

		
		ambientLight(102, 102, 102);
		lightSpecular(204, 204, 204);
		directionalLight(102, 102, 102, 0, 0, -1);
		specular(255, 255, 255);
		emissive(51, 51, 51);
		ambient(50, 50, 50);
		shininess(20.0f); 
		
//			float dirY = (mouseY / (float)height - 0.5f) * 2f;
//			float dirX = (mouseX / (float)width - 0.5f) * 2f;
//			p.directionalLight(204, 204, 204, -dirX, -dirY, -1);
		
		// draw star -----------------------
//		p.pushMatrix();
//		p.noStroke();
//		p.translate(p.width/2, p.height/2, 0);
//		p.rotateY(angle);  
//		p.sphereDetail(300);
//		Shapes.drawStar(p, 8, 150, 0.5f, 100, 0);
//		// p.sphere(200);  
//		angle += 0.01;
//		p.popMatrix();
		
		// draw circle
		DrawUtil.setCenterScreen(p);
		p.pushMatrix();
//		drawEQ(40,3,50,200,5);
		drawEQSmoothed(30,4,50,200,15,5,2);
		p.popMatrix();
	}
	
	protected void drawEQ(int numBands, int discReso, float radius, float spacing, float amp) {
		float startX = -spacing * numBands/2f;
		
		p.noStroke();
		p.fill(200, 200, 200);

		p.rotateY(p.mouseX/100f);
		p.rotateX(p.mouseY/100f);
		
		// draw EQ
		float radSegment = P.TWO_PI / discReso;
		for (int i = 1; i < numBands; i++) {
			float lastEqVal = radius + radius * amp * _audioInput.getFFT().spectrum[i-1];
			float eqVal = radius + radius * amp * _audioInput.getFFT().spectrum[i];
			float curX = startX + i * spacing;
			float lastX = startX + (i-1) * spacing;
			
			p.beginShape(P.TRIANGLE_STRIP);
			for (int j = 0; j <= discReso; j++) {
				float curRads = j * radSegment + (i/10f); // last bit for a twist
				p.vertex(lastX, P.sin(curRads) * lastEqVal, P.cos(curRads) * lastEqVal);
				p.vertex(curX, P.sin(curRads) * eqVal, P.cos(curRads) * eqVal);
			}
			p.endShape();
		}

	}

	protected void drawEQSmoothed(int numBands, int discReso, float radius, float spacing, float smoothsteps, float amp, float smoothEasing) {
		float startX = -spacing * numBands/2f;
		
		p.noStroke();
		p.fill(255, 255, 255);
		
		int from = p.color(0);
		int to = p.color(255);

//		p.stroke(200, 200, 200);
//		p.fill(0, 0, 0);
//		p.noFill();
		p.rotateY(p.mouseX/100f);
		p.rotateX(p.mouseY/100f);

		// draw EQ
		float spacingSubDiv = spacing / (smoothsteps);
		float radSegment = P.TWO_PI / discReso;
		for (int i = 1; i < numBands; i++) {
			
			float lastEqVal = radius + radius * amp * _audioInput.getFFT().spectrum[i-1];
			float eqVal = radius + radius * amp * _audioInput.getFFT().spectrum[i];
			float curX = startX + i * spacing;
			float lastX = startX + (i-1) * spacing;
			
			float ampDiff = eqVal - lastEqVal;
			
			p.fill( p.lerpColor(from, to, (float)i/numBands) );
			
//			P.println("lastEqVal",lastEqVal);
//			P.println("eqVal",eqVal);
//			P.println("ampDiff",lastEqVal+ampDiff);
			
			for (float subDivision = 1; subDivision <= smoothsteps; subDivision++) {
				
				// interpolate the amplitude
				float lastEqSubDiv = lastEqVal + ampDiff * MathUtil.easePowPercent((subDivision-1f)/smoothsteps, smoothEasing);
				float curEqSubDiv = lastEqVal + ampDiff * MathUtil.easePowPercent((subDivision)/smoothsteps, smoothEasing);
				
				// break up subdivision spacing
				float subDivLastX = lastX + spacingSubDiv * (subDivision-1);
				float subDivCurX = lastX + spacingSubDiv * subDivision;
				
//				P.println("% ",(subDivision-1f)/smoothsteps);
//				P.println("% ",(subDivision)/smoothsteps);
//				if(subDivision == smoothsteps) P.println("curEqSubDiv",curEqSubDiv);
//				P.println("subDivLastX ",subDivLastX);
//				P.println("subDivCurX ",subDivCurX);
				
//				p.noFill();
//				if(subDivision == smoothsteps) p.fill(255, 255, 255);

				
				p.beginShape(P.TRIANGLE_STRIP);
				for (int j = 0; j <= discReso; j++) {
					float curRads = j * radSegment;
					p.vertex(subDivLastX, P.sin(curRads) * lastEqSubDiv, P.cos(curRads) * lastEqSubDiv);
					p.vertex(subDivCurX, P.sin(curRads) * curEqSubDiv, P.cos(curRads) * curEqSubDiv);
				}
				p.endShape();
				
			}
		}
		
	}
	

	public void keyPressed() {
		super.keyPressed();
		if(p.key == ' ') {
		}
	}


}
