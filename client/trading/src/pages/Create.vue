<template>
  <q-page>
    <div class="container-sm q-px-xl q-py-sm">
      <div class="row justify-center">
        <div class="col text-center " style="max-width:600px">
          <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
            <q-input
              clearable
                v-model= "name"
          label='Názov hry'
          hint='Zadaj názov hry'
          :rules="[ val => val && val.length > 0 || 'Nesmie zostať prádzne']"
              lazy-rules
            />
            <q-input
              clearable
              v-model= "radius"
          label='Polomer hry'
          hint='Zadaj polomer hry'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 1000 || 'Zadaj platný rádius']"
              lazy-rules
            />
      <q-input
        filled
        v-model="color"
        label='Farba hry'
        :rules="['anyColor']"
        hint="Vyber farbu hry"
      >
        <template v-slot:append>
          <q-icon name="colorize" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-color v-model="color" />
            </q-popup-proxy>
          </q-icon>
        </template>

      </q-input>
            <q-input
              clearable
          v-model= "num_players"
          label='Počet hráčov'
          hint='Zadaj maximálny počet hráčov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 50 || 'Zadaj platný počet']"
              lazy-rules

            />
            <q-input
              clearable
          v-model= "num_shops"
          label='Počet obchodov'
          hint='Zadaj počet obchodov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 20 || 'Zadaj platný počet']"
              lazy-rules

            />
            <q-input
              clearable
          v-model= "num_products"
          label='Počet produktov'
          hint='Zadaj počet produktov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 100 || 'Zadaj platný počet']"
              lazy-rules

            />   
            <q-input
              clearable
          v-model= "money"
          label='Základný kapitál'
          hint='Zadaj počet produktov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 100000 || 'HAHAHHA xDDD... Všetci vieme, že toľko nemáš. Daj menej...']"
              lazy-rules
            /> 

          </q-input>
            <q-input
              clearable
          v-model= "duration"
          label='Trvanie hry'
          hint='Zadaj trvanie hry v hodinách'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 50 || 'Zadaj platný počet']"
              lazy-rules
            />

            <div>
              <q-btn label="Submit" type="submit" color="primary" />
              <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
            </div>
          </q-form>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
    
    data() {
     return  {
          // name : this.$store.getters["global/game"].name,
          // radius : this.$store.getters["global/game"].radius,
          // color: this.$store.getters["global/game"].color,
          // num_players: this.$store.getters["global/game"].num_players,
          // num_shops: this.$store.getters["global/game"].num_shops,
          // num_products: this.$store.getters["global/game"].num_products,
          // money: this.$store.getters["global/game"].money,
          // color: this.$store.getters["global/game"].color,
          // duration: this.$store.getters["global/game"].duration,
          name : null,
          radius : null,
          color: null,
          num_players: null,
          num_shops: null,
          num_products: null,
          money: null,
          color: null,
          duration: null,
          longitude_center : null,
          latitude_center : null,
          finished_at : null,
          created_at : null,
          shops: null,
    }
  },
  methods: {

    updatePosition(position){
      this.longitude_center = position.coords.longitude
      this.latitude_center = position.coords.latitude
      this.safeSubmit()
    },

    errornotify(place, error){
          this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Submit failed on '+ place +' with ' + error,
          icon: 'report_problem'
        })
    },

    safeSubmit(){
      console.log('send ' +this.longitude_center),
      this.$axios.get('/create-game', {
        params: {
          max_players: this.num_players,
          max_shops: this.num_shops,
          max_products:this.num_products,
          game_time: this.duration,
          color: this.color,
          name: this.name,
          radius: this.radius,
          player_money: this.money,
          longitude_center: this.longitude_center,
          latitude_center: this.latitude_center,
        }
      })
      .then((response) => {
        var data = response.data
        this.created_at = data.created_at;
        this.finished_at = data.finished_at;
        this.id = data.id;
        this.$store.dispatch('global/initGame',data).then(() => {
          this.$store.dispatch('global/setCenter',data).then(() => {    
            this.$axios.get('/get-shops', {
              gameId: this.$store.getters['global/game'].id
              }).then((response) =>{
                this.$store.dispatch('global/addShops',response.data)
                this.shops = response.data;
                this.$router.push('/game')
              }).catch((e) =>{
                this.errornotify('/get-shops',e)
              })  
          })
        })
        
        
        this.$q.notify({
          color: "green-4",
          textColor: "white",
          icon: "videogame_asset", 
          message: "Vytvorená!"
        });

      })
      .catch((e) => {
        this.errornotify('/create-game',e)
      })
      
    },

    getLocation(){
      if (navigator.geolocation){
        navigator.geolocation.getCurrentPosition(this.updatePosition)
      } else {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'GPS error',
          icon: 'report_problem'
        })
      }
      
    },

    onSubmit() {
      //send to server
      this.getLocation()     
    },
    onReset() {
      this.name = this.$store.getters["global/game"].name,
      this.radius = this.$store.getters["global/game"].radius,
      this.color= this.$store.getters["global/game"].color,
      this.num_players= this.$store.getters["global/game"].num_players,
      this.num_shops= this.$store.getters["global/game"].num_shops,
      this.num_products= this.$store.getters["global/game"].num_products,
      this.money= this.$store.getters["global/game"].money,
      this.duration = this.$store.getters["global/game"].duration,
      this.longitude_center = null,
      this.latitude_center = null,
      this.accept= false,
      console.log(name)
    }
  },

  mounted: function() {
    this.onReset()
    console.log('long '+this.longitude_center)
  },

};
</script>
