#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform float u_time;

void main() {
    // Get the texture color
    vec4 texColor = texture2D(u_texture, v_texCoord);
    
    // Calculate the red tint based on time
    float redValue = sin(u_time * 10.0) * 0.5 + 0.5; // Adjust the frequency as needed
    vec3 redTint = vec3(1.0, redValue, redValue);
    
    // Apply the red tint
    vec4 finalColor = vec4(texColor.rgb * redTint, texColor.a);
    
    // Output the final color
    gl_FragColor = finalColor * v_color;
}
