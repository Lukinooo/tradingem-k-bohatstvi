<template>
  <q-page>
    
    <div class="container-sm q-px-xl q-py-sm">
      <div class="row justify-center">
        <div class="col text-center " style="max-width:600px">
          <q-list bordered>
          <q-item class="row no-wrap justify-between">  
                <q-item-section class="justify-start q-px-none text-left col-6">Produkt</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">Počet</q-item-section>

          </q-item>
          </q-list>
          <q-list bordered>
            <q-item class="row no-wrap justify-between" clickable v-ripple v-for="i in inventory" :key="i.name">  
                <q-item-section class="justify-start q-px-none text-left col-6">{{i.name}}</q-item-section>
                <q-item-section class="justify-start q-px-none text-center col-2">{{i.num}}</q-item-section>
            </q-item>

          </q-list>
        </div>
      </div>
    </div>
  </q-page>
</template>
  
<script>
export default {
  name: 'PageIndex',
    computed: {
    
    inventory()  {
        return this.$store.getters['global/inventory']
      }    
  },
}
</script>
