uniform vec4 m_Color;
void main(){
    //returning the color of the pixel (here solid blue)
    //- gl_FragColor is the standard GLSL variable that holds the pixel

        gl_FragColor = m_Color;        
}