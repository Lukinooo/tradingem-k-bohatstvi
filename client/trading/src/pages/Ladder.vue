<template>
  <q-page>
    <div class="container-sm q-px-sm q-py-sm">
      <div class="row justify-center">
        <div class="col text-center " style="max-width:600px">

          <q-list bordered>
          <q-item class="row no-wrap justify-between q-px-none q-px-sm">  
                <q-item-section class="justify-start q-px-none text-left col-5">Nick</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">ID</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">Skóre</q-item-section>
          </q-item>
          </q-list>

          <q-list bordered>
            <q-scroll-area style="height: 500px;">
              <q-item class="row no-wrap justify-between q-px-none q-px-sm" v-ripple v-for="p in players" :key="p.playerId" :id="p.playerId">  
                  <q-item-section class="justify-start q-px-none text-left col-5">{{p.playerName}}</q-item-section>
                  <q-item-section class="justify-start q-px-none text-center col-2">{{p.playerId}}</q-item-section>
                  <q-item-section class="justify-start q-px-none text-center col-2">{{parseFloat(p.playerMoney).toFixed(2)}}</q-item-section>
              </q-item>
            </q-scroll-area>
          </q-list>
        </div>
      </div>
    </div>

  </q-page>
</template>

<script>
export default {
  data(){
    return {
      players : null,
    }
  },
  computed: {

    player_id() {
      return this.$store.getters['global/player_id']
    }
  },
  mounted: function () {
    this.$axios.get('/get-players-score', {
      params : {
        game_id : this.$store.getters['global/game'].id
      }
    }).then((response) => {
      this.players = response.data;
      //console.log('ladder data '+ JSON.stringify(response.data));
      //console.log('ladder '+response.data);
    }).catch((e)=> {
      this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Submit failed on '+ e,
          icon: 'report_problem' 
        })
    })
  },
  updated: function(){
    document.getElementById(this.$store.getters['global/player_id']).style.backgroundColor = this.$store.getters['global/game'].color
  } 
}


</script>
