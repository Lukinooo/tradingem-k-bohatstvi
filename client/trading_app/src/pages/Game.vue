<template>
  <q-page class = "full-width column   items-center">
        <canvas id="map" width="500" height="500"></canvas> 
  </q-page>
  
</template>




<script>
// import createjs from './node_modules/createjs/1.0.0/createjs.min.js'
import createjs from 'createjs-module/createjs'

export default {
  
  
  name: 'PageIndex',

  beforeMount: function(){
   
  },
  mounted: function(){
    // document.getElementById('map').
    this.paint()
    
  },
  methods : {
    paint(){
    const vw = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
    const vh = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    document.getElementById('map').width= vw;
    document.getElementById('map').height= vh;  
    var width = document.getElementById('map').clientWidth;
    var height = document.getElementById('map').clientHeight;
    var stage = new createjs.Stage("map");
    var square = new createjs.Shape();
    square.graphics.beginFill('Blue').drawRoundRect(-width/2, -height/2, width, height, 0);
    square.x = width/2;
    square.y = height/2;
    stage.addChild(square);
    stage.update();
    },
  },
  data () {
    return {
      color: 'blue',
    }
  } 
}
</script>

