<template>
  <q-page>
    <div class="container-sm q-px-xl q-py-sm">
      <div class="row justify-center">
        <div class="col text-center " style="max-width:600px">
          
      
    
          <q-list bordered>
          <q-item class="row no-wrap justify-between q-px-none q-px-sm">  
                <q-item-section class="justify-start q-px-none text-left col-4">Názov hry</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">ID</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">Počet hráčov</q-item-section>
                <q-item-section class="justify-end q-px-none text-right col">Farba</q-item-section>

          </q-item>
          </q-list>
          <q-list bordered>
            <q-scroll-area style="height: 500px;">
              <q-item class="row no-wrap justify-between q-px-none q-px-sm" clickable v-ripple v-for="game in data" :key="game.id" @click="join(game.id)">  
                  <q-item-section class="justify-start q-px-none text-left col-4">{{game.name}}</q-item-section>
                  <q-item-section class="justify-start q-px-none text-center col-2">{{game.id}}</q-item-section>
                  <q-item-section class="justify-start q-px-none text-center col-2">{{game.max_player}}</q-item-section>
                  <q-item-section avatar class="justify-end q-px-none text-right col">
                    <span style="height: 40px; width: 40px; background-color: #bbb; border-radius: 50%; display: inline-block;" v-bind:style="{ 'background-color': game.color, }"> </span>
                    <!-- <q-avatar :color="red" text-color="white" /> -->
                  </q-item-section>
              </q-item>
            </q-scroll-area>
          </q-list>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import Axios from 'axios';
export default {
  data() {
    return {
      data: null,
      games: [
       
        {
          id: 1,
          color: "blue",
          name: "pre vsetkych",
          players: 5,
        },
        {
          id: 2,
          color: "blue",
          name: "iba pre kamaratov",
          players: 3,
        },
        {
          id: 3,
          color: "red",
          name: "chodte vsetci do discordu",
          players: 4,
        },
        {
          id: 4,
          color: "green",
          name: "narodeninova oslava",
          players: 3,
        },
        {
          id: 5,
          color: "pink",
          name: "zachody v auparku",
          players: 3,
        }
      ],
      joined : {
      active : true,
      id : 2,
      start : "12:15",
      end : "12:30",
      name: 'Pasívne príjmy',
      radius : 200,
      color : '#027be3',
      num_players : 5,
      num_shops : 3,
      num_products : 5,
      money : 1000,
      gps : {
        latitude : 48.190492, 
        longitude : 17.293099,
      },
      shops : [
        {
          id : 1,
          gps : {
            latitude : 48.190586,
            longitude : 17.292928,
          },
        },
        {
          id : 2,
          gps : {
            latitude : 48.190533,
            longitude : 17.293166,
          },
        },
        {
          id : 3,
          gps : {
            latitude : 48.190227, 
            longitude : 17.293284,
          },
        }
      ]
    },
    };
  },
  methods : {
    join(id){
      console.log('clicked game'+ JSON.stringify(this.data.find(e => e.id === id)))
      //get details of game into store
      this.joined = this.data.find(e => e.id === id);
      this.$store.dispatch('global/initGame', this.joined).then(()=>{
        console.log(JSON.stringify(this.joined))

        this.$axios.get('/join-game',{
          params : {
          player_name : this.$store.getters['global/nick'],
          game_id : this.$store.getters['global/game'].id 
          }
        })
      .then((response) => {
        this.data = response.data
        this.$store.dispatch('global/join',data).then((response)=>{
          console.log('response '+JSON.stringify(response.data))
          this.$router.push('/game')
          this.$q.notify({
            color: 'positive',
            position: 'top',
            message: 'Si pripojený!',
            icon: 'done'
          })
        })
        
      })
      .catch((e) => {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Problém s pripojením' + e,
          icon: 'report_problem'
        })
      })
      
      })
      
    }
  },
  
  mounted: function(){
      this.$axios.get('/list-games')
      .then((response) => {
        this.data = response.data
        console.log('response '+JSON.stringify(response.data))
      })
      .catch(() => {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Loading failed',
          icon: 'report_problem'
        })
      })
  }
}

</script>
