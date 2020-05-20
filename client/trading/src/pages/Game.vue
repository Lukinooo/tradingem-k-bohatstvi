<template>
  <q-page class = "full-width column   items-center">
    <q-dialog v-model="alert">
      <q-card>
        <q-card-section>
          <div class="text-h6">Alert</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          Nie si prihlásený do žiadnej hry!
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="OK" color="primary" v-close-popup to="/join" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="showShop" >
      <q-card style="width:100%; height:100%">
        <q-card-section class="full-width space-between row items-center">
          <span class="col text-h6 text-left">{{nearest.name}}</span>
          <span class="col text-h7 text-right">{{money}} €</span>
        </q-card-section>

        <q-separator />

        <q-card-section class="q-pt-none">
           <q-list>
                <q-item class="row no-wrap justify-between q-px-none " style="max-width: 600px;">  
                  <q-item-section class="q-px-none text-left col-2">Názov</q-item-section>
                  <q-item-section class="q-px-none text-center col-2">Cena</q-item-section>
                  <q-item-section class="q-px-none text-center col-2">Pocet</q-item-section>
                </q-item>
            </q-list>
            <q-separator />
            <q-list style="height=400px"> 
              
              <q-scroll-area style="height: 400px;">
                <!-- class="row no-wrap justify-between q-px-none items-center" -->
                <q-item  class="full-width column items-center content-center"
                v-for="p in shopProducts" :key="p.id" 
                >  

                  <q-item class="full-width row no-wrap justify-between q-px-none " style="max-width: 600px;">  
                    <q-item-section class="q-px-none text-left col-2">{{p.name}}</q-item-section>
                    <q-item-section class="q-px-none text-center col-2">{{p.price}} €</q-item-section>
                    <q-item-section class="q-px-none text-center col-2">{{p.count}}</q-item-section>
                  </q-item>
                  <q-item-section class="row">
                    <q-item-section class="q-px-none text-center col-5">
                    <q-btn-group push>
                        <q-btn :loading="loadingBuy" push label="Nákup" icon="cloud_download" @click="buyProd(p, nearest.id)"/>
                        <q-btn :loading="loadingSell" push label="Predaj" icon="cloud_upload" @click="sellProd(p,nearest.id)"/>
                      </q-btn-group>
                    </q-item-section>
                  </q-item-section>
                  <hr style="width:90%">

                </q-item>
              </q-scroll-area>
            </q-list>
        </q-card-section>
        <q-separator />
        <q-card-actions align="right">
          <q-btn flat label="OK" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="isEnd" persistent>
      <q-card class="my-card">
        <q-img src="statics/quote.jpg" />

        <q-card-section>
          
          <div class="row no-wrap items-center">
            <div class="col text-h6 ellipsis">
              {{message}}
            </div>
          </div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            €・{{money}}
          </div>
          <div class="text-caption text-grey">
            {{subtitle}}
          </div>
        </q-card-section>

        <q-separator />

        <q-card-actions align="right">
          <q-btn v-close-popup flat color="primary" round icon="exit_to_app" to="/" />
        </q-card-actions>
      </q-card>
    </q-dialog>

        <canvas id="map" width="500" height="500"></canvas> 
  </q-page>
  
</template>




<script>
// import createjs from './node_modules/createjs/1.0.0/createjs.min.js'
import createjs from 'createjs-module/createjs'

export default {
  
  
  name: 'PageIndex',

  data () {
    return {
      message : '',
      subtitle :'',
      interval : null,
      isEnd : false,
      loadingBuy : false,
      loadingSell : false,
      showShop: false,
      shopProducts: { '0' : 0},
      color: 'blue',
      stage: null,
      position_child :null,
      shops : null,
      vw : Math.max(document.documentElement.clientWidth, window.innerWidth || 0),
      vh : Math.max(document.documentElement.clientHeight, window.innerHeight || 0),
      x_min_dist : null,
      y_min_dist : null,
      x_max_dist : null,
      y_max_dist : null,
      x_interval : null,
      y_interval : null,
      position : {
        latitude : 48.1903661,
        longitude : 17.2930368,
      },
      newpos : {
        latitude : 48.1903661,
        longitude : 17.2930368,
      },
      plus : true,
      alert : false,
      gps_allert : true,
      nearest : { name : 'name'},
      timestamp :'',
    }
  },

  created() {
      setInterval(this.getNow,1000)
      setInterval(this.checkEnd,10000)
  },

  computed: {
      game() {
        return this.$store.getters['global/game']
      },
      money(){
        return this.$store.getters['global/game'].money
      },
      player_id(){
        return this.$store.getters['global/player_id']
      }
      // shopProducts(){
      //   return { '0' : 0}
      // },
  },
  mounted: function(){
    // document.getElementById('map').
    // console.log('mounted')
    if (this.game.active){
      this.paint()
      // console.log('zebrak');
      // console.log('navi' + JSON.stringify(navigator.geolocation));
      if (navigator.geolocation){
        this.interval = window.setInterval(() => {
          var options = {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0
          };
          navigator.geolocation.getCurrentPosition(this.updatePlayerPosition,this.errornotify,options)
        }, 6000)
            // navigator.geolocation.watchPosition(this.updatePlayerPosition)
      }else{
        // console.log('navi' + JSON.stringify(navigator.geolocation));
        this.errornotify('GPS init','NOT AVAILABLE')
      }
    } else {
      // console.log('alert '+this.alert)
      this.alert = true;
    }
    
    
  },
  methods : {

     getNow() {
      const today = new Date();
      // const date = +'-'+(today.getMonth()+1)+'-'+today.getDate();
      // const time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
      var obj = {
        year : today.getFullYear(),
        month : today.getMonth()+1,
        day : today.getDate(),
        hour : today.getHours(),
        minute : today.getMinutes(),
        seconds : today.getSeconds(),
      }

      this.timestamp = obj;
      // console.log('time '+ JSON.stringify(this.timestamp))
    },

    checkEnd(){
      var e = this.game.finished_at
      var t = this.timestamp
      var end = new Date(e.year, e.monthValue, e.dayOfMonth, e.hour, e.minute, e.second)
      var now = new Date(t.year, t.month, t.day, t.hour, t.minute, t.seconds)
      if (end < now){
        this.isEnd = true
      }
      // this.isEnd = true;
      if (this.isEnd){
        window.clearInterval(this.interval)
        this.$axios.get('/get-players-score',{
          params : {
            game_id : this.game.id
          }
        }).then((response)=>{
          // console.log('players[0] ' + JSON.stringify(response.data[0]) + " player_id" + this.player_id)
          if (response.data[0].playerId === this.player_id){
            this.message = "Vyhral si!"
            this.subtitle = "“If it ain’t making me money, making me better or making me happy… ain’t making time for it.” – 50 Cent"
          } else {
            this.message = "Prehral si :'("
            this.subtitle = "“If you don't use your time, the time uses you, as a result; you are the loser”― Ehsan Sehgal"
          }
        })
      }
      
    },

    buyProd(product,shopId){
      if (product.count == 0){
        return
      }
      this.loadingBuy = true;
      // console.log('buy prod'+ JSON.stringify(product) + ' in shop ' + shopId);
      // this.positnotify('buy prod'+ JSON.stringify(product) + ' in shop ' + shopId)
      this.$store.dispatch('global/buyProduct',product)
      .then((money)=>{
        
        this.$axios('/buy-product',{
        params :{
          game_id : this.game.id,
          player_id  : this.player_id,
          shop_id : shopId,
          product_id : product.id
        }
        }).then((response)=>{

          this.loadShop(shopId)
          .then(()=>{
            this.positnotify('OK')
            // console.log('buy prod new money '+ JSON.stringify(money))
            this.loadingBuy = false     
          }).catch(()=>{
            this.errornotify('/buy-product',"couldn't load shop")
            this.loadingBuy = false     
          })

        }).catch((e)=>{
          this.errornotify('/buy-product',e)
          this.loadingBuy = false     
        })

      }).catch((e)=>{
        this.errornotify('global/buyProduct',e)
        // console.log('buy prod global/buyProduct' + e + ' ' + JSON.stringify(money))
        this.loadingBuy = false     
      })
      
    },

    sellProd(product,shopId){
        this.loadingSell = true,
        // console.log('sell prod'+ JSON.stringify(product) + ' in shop ' + shopId);
        // this.positnotify('sell prod'+ JSON.stringify(product) + ' in shop ' + shopId)
        this.$store.dispatch('global/sellProduct',product).then((money)=>{
          
          this.$axios.get('/sell-product',{
                          params : {
                            game_id : this.game.id,
                            player_id  : this.player_id,
                            shop_id : shopId,
                            product_id : product.id
                          }
          }).then((response)=>{

            this.loadShop(shopId)
            .then(()=>{
              this.positnotify('OK')
              // console.log('sell prod new money '+ JSON.stringify(money))
              this.loadingSell = false
            }).catch(()=>{
              this.errornotify('/sell-product',"couldn't load shop")
              this.loadingSell = false
            })

          }).catch((e)=>{
            this.errornotify('/sell-product',e)
            this.loadingSell = false
          })

        }).catch((e)=>{
          this.errornotify('global/sellProduct',e)
          // console.log('buy prod global/sellProduct ' + e)
          this.loadingSell = false
        })
        
    },

    positnotify(message){
          this.$q.notify({
          color: 'positive',
          position: 'top',
          message: message,
          icon: 'done'
        })
    },

    errornotify(place = 'gps', error){
          this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Submit failed on '+ place +' with ' + error,
          icon: 'report_problem'
        })
    },
    //https://www.geodatasource.com/developers/javascript
    //in m
    realDistance(lat1, lon1, lat2, lon2) {
      if ((lat1 == lat2) && (lon1 == lon2)) {
        return 0;
      }
      else {
        var radlat1 = Math.PI * lat1/180;
        var radlat2 = Math.PI * lat2/180;
        var theta = lon1-lon2;
        var radtheta = Math.PI * theta/180;
        var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        if (dist > 1) {
          dist = 1;
        }
        dist = Math.acos(dist);
        dist = dist * 180/Math.PI;
        dist = dist * 60 * 1.1515;
        // if (unit=="K") { dist = dist * 1.609344 }
        // if (unit=="N") { dist = dist * 0.8684 }
        //always in m => km * 1000
        dist = dist * 1.609344 * 1000;
        return dist;
      }
    },

    loadShop(id){
      return new Promise ((resolve, reject)=>{
        setTimeout(()=>{
          this.$axios.get('/get-shop-products',{
              params : {
                shop_id : id
              }
            }).then((response)=>{
              // console.log('SHOP PRODUCTS '+JSON.stringify(response.data));
              this.shopProducts = response.data
              // console.log('THIS PRODUCTS '+ JSON.stringify(this.shopProducts));
              this.showShop = true;
              resolve()
            }).catch((e) => {
              this.errornotify('/get-shop-products',e)
              reject()
            })
        },100)
      })
    },

    nearestShop(){
      var shops = this.game.shops;
      // console.log('shops ' + JSON.stringify(shops));
      var min = {dist: 2000};
      for (var i = 0; i < this.game.num_shops; i++) {
        var e = shops[i]
        var dist = this.realDistance(e.latitude, e.longitude, this.position.latitude, this.position.longitude);
        // console.log('dist ' + dist )
        if ( dist < min.dist){
          min = e;
          min.dist = dist;
        }
      };

      min.dist = 20
      // console.log(' WARNING HARD-CODED nearest dist ' + min.dist )

      if (min.dist < 25){
        this.nearest = min;
        this.loadShop(min.id)
      }
    },

    getPosition(){
      if (this.game.active){
          // console.log('position update');
          if (this.plus){
            this.position.latitude += Math.random() *0.001;
            this.position.longitude += Math.random() *0.001;
          }else{
            this.position.latitude -= Math.random() *0.001;
            this.position.longitude -= Math.random() *0.001;
          }
          // console.log('navi' + JSON.stringify(navigator.geolocation));
          if (navigator.geolocation){
            navigator.geolocation.getCurrentPosition(this.updatePlayerPosition)
          }else{
            this.gps_allert = true;
          }
          
      }
      
    },
    //[0,0] je vlavo hore
    calcBounds(){
      var shops = this.game.shops
      // console.log('obchody' + JSON.stringify(shops));
        for (var i = 0; i < this.game.num_shops; i++) {
          // var dist = calcDist(shops[i].longitude,shops[i].latitude, this.game.gps.longitude,this.game.gps.latitude)
          var x_dist = this.calcDist(shops[i].longitude, this.game.gps.longitude)
          var y_dist = this.calcDist(shops[i].latitude, this.game.gps.latitude)
          if (x_dist > this.x_max_dist ){
            this.x_max_dist = x_dist;
          }
          if (y_dist < this.y_max_dist ){
            this.y_max_dist = y_dist;
          }
          if (x_dist < this.x_min_dist){
            this.x_min_dist = x_dist;
          }
          if (y_dist > this.y_min_dist){
            this.y_min_dist = y_dist;
          }
        }
        this.x_interval = this.x_max_dist - this.x_min_dist;
        this.y_interval = this.y_max_dist - this.y_min_dist;
    },
    calcDist(x,y){
      return x-y
    },

    calcPosOnMap(latitude, longitude){
      if (this.x_min_dist == null){
        this.calcBounds()
      }
      // console.log('calc pos')

      var width = document.getElementById('map').clientWidth;
      var height = document.getElementById('map').clientHeight;
      // calcDist(longitude,latitude,this.game.gps.longitude, this.game.gps.latitude)
      //ziskanie vzdialenosti x a y
      var x_dist = this.calcDist(longitude,this.game.gps.longitude) 
      var y_dist = this.calcDist(latitude,this.game.gps.latitude)
      //tuto vzdialenost treba interpolovat, podla intervalu medzi min max
      //(x_dist - this.x_min_dist)/this.x_interval;
      //(y_dist - this.y_min_dist)/this.y_interval;
      var bot = 0.2;
      var top = 0.8;
      var x1_inter = bot + (x_dist - this.x_min_dist)/(this.x_interval/(top-bot));
      var y1_inter = bot + (y_dist - this.y_min_dist)/(this.y_interval/(top-bot));
      //previest do suradnicoveho systemu
      // console.log('calc pos'+ JSON.stringify({x: x1_inter*width, y: y1_inter*height}))
      return {x: x1_inter*width, y: y1_inter*height}
    },

    updatePlayerPosition(position){
      // console.log('player: '+ position.coords.latitude +" | "+position.coords.longitude);
      
      
      var pos = this.calcPosOnMap(position.coords.latitude,position.coords.longitude);
      this.newpos = {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude,
      }
      this.$q.notify({
          color: "red-5",
          textColor: "white",
          icon: "warning",
          message: JSON.stringify(this.newpos) + "\n"+ JSON.stringify(this.position)
        });
      this.position.latitude = position.coords.latitude,
      this.position.longitude = position.coords.longitude,
      this.updateObjPosition(this.position_child,pos),
      this.nearestShop()
    },

    updateObjPosition(bitmap,pos){
      // console.log('act pos'+JSON.stringify(pos))
      bitmap.x = pos.x-bitmap.scaleX/2;
      bitmap.y = pos.y-bitmap.scaleY/2;
    },

    setObjPosition(bitmap,pos,coef = 0.005){
      var width = document.getElementById('map').clientWidth;
      var height = document.getElementById('map').clientHeight;
      bitmap.scaleX = width*coef;
      bitmap.scaleY = width*coef;
      this.updateObjPosition(bitmap,pos);
    },

    paint(){
    document.getElementById('map').width= this.vw;
    document.getElementById('map').height= this.vh;  
    var width = document.getElementById('map').clientWidth;
    var height = document.getElementById('map').clientHeight;
    this.stage = new createjs.Stage("map");
    var square = new createjs.Shape();
    square.graphics.beginFill(this.game.color).drawRoundRect(-width/2, -height/2, width, height, 0);
    square.x = width/2;
    square.y = height/2;
    this.stage.addChild(square);

    var doge = new Image();
    doge.src = "statics/game/shop.svg"; 
    // console.log('ahoj');
    for (var i = 0; i < this.game.num_shops; i++) {
      // console.log('pos '+ i)
      var bitmap = new createjs.Bitmap(doge);

      //compute position on map from center
      var pos = this.calcPosOnMap(this.game.shops[i].latitude, this.game.shops[i].longitude)
      this.setObjPosition(bitmap,pos);
      this.stage.addChild(bitmap);
    }
    // console.log('my position')
    var me = new Image();
    me.src = "statics/game/location.svg"; 
    var bitmap = new createjs.Bitmap(me);

    //compute position on map from center
    var pos = this.calcPosOnMap(this.position.latitude, this.position.longitude)
    this.setObjPosition(bitmap,pos,0.002)
    this.position_child = bitmap;
    this.stage.addChild(this.position_child);

    createjs.Ticker.on("tick", this.stage);
    // this.stage.scaleX = -1;
    this.stage.update(); 
    },
  },
  
}
</script>

