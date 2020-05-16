<template>
  <q-page class="flex flex-center">
    
    <div>
      <q-btn
      @click="paint()"
      >
      Kresli!
      </q-btn>
      Game canvas page
      
        <canvas id="demoCanvas" width="500" height="300"></canvas>
      
    </div>
  </q-page>
  
</template>




<script>
export default {
  name: 'PageIndex',
  mounted: function(){
  },
  methods : {
    paint(){
      var stage = new createjs.Stage("demoCanvas");
    var circle = new createjs.Shape();
    circle.graphics.beginFill("DeepSkyBlue").drawCircle(0, 0, 50);
    circle.x = 100;
    circle.y = 100;
    stage.addChild(circle);
    stage.update();
    },
  }
}
</script>

