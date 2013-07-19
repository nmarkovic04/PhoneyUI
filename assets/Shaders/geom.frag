uniform vec4 m_Color;
void main(){
    //returning the color of the pixel (here solid blue)
    //- gl_FragColor is the standard GLSL variable that holds the pixel
    //color. It must be filled in the Fragment Shader.
    if(gl_PointCoord.y==0.0f){
        gl_FragColor = m_Color;        

    }else{
        gl_FragColor= vec4(0.0f, 0.0f, 0.0f, 1.0f);
    }
}