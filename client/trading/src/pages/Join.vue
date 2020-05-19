<template>
  <q-page>
    <div class="container-sm q-px-xl q-py-sm">
      <div class="row justify-center">
        <div class="col text-center " style="max-width:600px">
          <q-list bordered>
          <q-item class="row no-wrap justify-between">  
                <q-item-section class="justify-start q-px-none text-left col-6">Názov hry</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">ID</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">Počet hráčov</q-item-section>
                <q-item-section class="justify-end q-px-none text-right col">Farba</q-item-section>

          </q-item>
          </q-list>
          <q-list bordered>
            
            <q-item class="row no-wrap justify-between" clickable v-ripple v-for="game in games" :key="game.id" @click="join(game.id)">  
                <q-item-section class="justify-start q-px-none text-left col-6">{{game.name}}</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">{{game.id}}</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">{{game.players}}</q-item-section>
                <q-item-section avatar class="justify-end q-px-none text-right col">
                  <q-avatar :color="game.color" text-color="white" />
                </q-item-section>
              
                
              
            </q-item>

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
      console.log('clicked game'+ JSON.stringify(this.games.find(e => e.id === id)))
      //get details of game into store
      console.log(JSON.stringify(this.joined))
      this.$store.dispatch('global/joinGame', this.joined)
      this.$router.push('/game')
    }
  },
  
  mounted: function(){
      this.$axios.get('/list-games')
      .then((response) => {
        console.log('response '+JSON.stringify(response))
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
