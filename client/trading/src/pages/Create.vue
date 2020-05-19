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

            <q-toggle v-model="accept" true-value="Verejná" false-value="Súkromná" :label="accept"/>

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
    return {
          name : this.$store.getters["global/game"].name,
          radius : this.$store.getters["global/game"].radius,
          color: this.$store.getters["global/game"].color,
          num_players: this.$store.getters["global/game"].num_players,
          num_shops: this.$store.getters["global/game"].num_shops,
          num_products: this.$store.getters["global/game"].num_products,
          money: this.$store.getters["global/game"].money,
          accept: true,
          color: this.$store.getters["global/game"].color,
          secondColor: '#027be3'
        }
  },
  methods: {
    onSubmit() {
        //send to server
        this.$q.notify({
          color: "green-4",
          textColor: "white",
          icon: "videogame_asset", 
          message: "Vytvorená!"
        });
    },
    onReset() {
      this.name = this.$store.getters["global/game"].name,
      this.radius = this.$store.getters["global/game"].radius,
      this.color= this.$store.getters["global/game"].color,
      this.num_players= this.$store.getters["global/game"].num_players,
      this.num_shops= this.$store.getters["global/game"].num_shops,
      this.num_products= this.$store.getters["global/game"].num_products,
      this.money= this.$store.getters["global/game"].money,
      this.accept= false,
      console.log(name)
    }
  },

  mounted() {
  },

};
</script>
