<template>
  <q-page class="flex flex-center">
    
    <div>
      Game canvas page
      <canvas id="demoCanvas" width="500" height="300"></canvas>
    </div>
  </q-page>
  
</template>


<script src="game.js"></script>
<script>
import EaselJS from easeljs

export default {
  name: 'PageIndex',
  mounted: function(){
    var stage = new createjs.Stage("demoCanvas");
      var circle = new createjs.Shape();
      circle.graphics.beginFill("DeepSkyBlue").drawCircle(0, 0, 50);
      circle.x = 100;
      circle.y = 100;
      stage.addChild(circle);
      stage.update();
  },
  methods : {
    init() {
      
    }
  }
}
</script>

