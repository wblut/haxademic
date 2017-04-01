#define PROCESSING_TEXTURE_SHADER

#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 resolution;
uniform sampler2D texture;
varying vec4 vertTexCoord;
uniform float offset;
uniform float sides;

void main(void) {
  //vertTexCoord (0..1,0..1) to  centered image coordinate (-width/2..width/2, - height/2..height/2)
  vec2 p =  vec2((vertTexCoord.x-0.5)*resolution.x,(vertTexCoord.y-0.5)*resolution.y);
  
  //centered image coordinate to radial coordinates
  float r = length(p);
  float phi = atan(p.y, p.x);
  
  //change angle from (0..2Pi) to (0..slice/2)
  float twopi = 6.283185;
  float slice=twopi/sides;
  phi = mod(phi,slice);// to (0..slice)
  phi = abs(phi - 0.5*slice)+offset;// to(0..slice/2)

  //lookup point back to cartesian
  p = r * vec2(cos(phi), sin(phi));

  // p/resolution+0.5 = from centered imagec oodiantes back to vertTexCoord
  
  gl_FragColor =  texture2D(texture, p/resolution+0.5);
}