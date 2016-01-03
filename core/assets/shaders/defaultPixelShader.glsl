// http://www.alcove-games.com/opengl-es-2-tutorials/lightmap-shader-fire-effect-glsl/
// https://www.filterforge.com/filters/2312.jpg

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 vColor;
varying vec2 vTexCoord;

uniform sampler2D u_texture; // Diffuse map

void main() {
	vec4 DiffuseColor = texture2D(u_texture, vTexCoord);
	gl_FragColor = vColor * DiffuseColor;
}