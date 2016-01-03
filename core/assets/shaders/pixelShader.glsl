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

// Texture samplers
uniform sampler2D u_texture; // Diffuse map
uniform sampler2D u_lightmap; // Light map

// Additional parameters for the shader
uniform vec2 resolution; // Resolution of screen
uniform LOWP vec4 ambientColor; // Ambient RGB, alpha channel is intensity 

void main() {
	vec4 diffuseColor = texture2D(u_texture, vTexCoord);
	vec2 lightCoord = (gl_FragCoord.xy / resolution.xy);
	vec4 light = texture2D(u_lightmap, lightCoord);
	
	// gl_FragColor = vColor * light;
	
	vec3 ambient = ambientColor.rgb * ambientColor.a;
	vec3 intensity = ambient + light.rgb;
 	vec3 finalColor = diffuseColor.rgb * intensity;
	
	gl_FragColor = vColor * vec4(finalColor, diffuseColor.a);
}
