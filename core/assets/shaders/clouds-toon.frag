// ----------------------------------------------------------------------------------------
//	"Toon Cloud" by Antoine Clappier - March 2015
//
//	Licensed under:
//  A Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
//	http://creativecommons.org/licenses/by-nc-sa/4.0/
//  https://www.shadertoy.com/view/4t23RR
// ----------------------------------------------------------------------------------------
#ifdef GL_ES
precision mediump float;
#endif

uniform float u_time;
uniform vec2 u_resolution;

varying vec4 v_color;
varying vec3 v_position;
varying vec2 v_texCoord;

#define TAU 6.28318530718

const vec3 backColor = vec3(0.0, 0.4, 0.58);
const vec3 cloudColor = vec3(0.18,0.70,0.87);

float f1(float px)
{
	return 0.6 * (0.5 * sin(0.1 * px) + 0.5 * sin(0.553 * px) + 0.7 * sin(1.2 * px));
}

float f2(float px)
{
	return 0.5 + 0.25 * (1.0 + sin(mod(40.0 * px, TAU)));
}

float layer(vec2 pq, float pt)
{
	vec2 qt = 3.5 * pq;
	pt *= 0.5;
	qt.x += pt;

	float xi = floor(qt.x);
	float xf = qt.x - xi -0.5;

	vec2 c;
	float yi;
	float d = 1.0 - step(qt.y, f1(qt.x));

	// Disk:
	yi = f1(xi + 0.5);
	c = vec2(xf, qt.y - yi );
	d =  min(d, length(c) - f2(xi + pt / 80.0));

	// Previous disk:
	yi = f1(xi + 1.0 + 0.5);
	c = vec2(xf - 1.0, qt.y - yi);
	d =  min(d, length(c) - f2(xi + 1.0 + pt / 80.0));

	// Next Disk:
	yi = f1(xi - 1.0 + 0.5);
	c = vec2(xf + 1.0, qt.y - yi);
	d =  min(d, length(c) - f2(xi - 1.0 + pt / 80.0));

	return min(1.0, d);
}

void main()
{
//    vec2 res = vec2(160.0, 90.0);
    vec2 res = u_resolution;
    vec2 fragCoord = vec2(v_texCoord.x, 1.0 - v_texCoord.y) * res.xy;
	vec2 uv = 2.0 * (fragCoord.xy - res.xy / 2.0) / min(res.x, res.y);
//	vec2 uv = 2.0 * (gl_FragCoord.xy - res.xy / 2.0) / min(res.x, res.y);
	vec3 color = backColor;

	for (float j = 0.0; j <= 1.0; j += 0.2) {
		vec2 lp = vec2(0.0, 0.3 + 1.5 * (j - 0.5));
		float lt = u_time * (0.5  + 2.0 * j) * (1.0 + 0.1 * sin(226.0 * j)) + 17.0 * j;
		float l = layer(uv + lp, lt);

		float blur = 4.0 * (0.5 * abs(2.0 - 5.0 * j)) / (11.0 - 5.0 * j);
		float v = mix(0.0, 1.0, 1.0 - smoothstep(0.0, 0.01 + 0.2 * blur, l));
		vec3 lc = mix(cloudColor, vec3(1.0), j);

		color = mix(color, lc, v);
	}

	gl_FragColor = vec4(color, 1.0);
//	gl_FragColor = vec4(v_texCoord.x, v_texCoord.y, 0.0, 1.0);
}
