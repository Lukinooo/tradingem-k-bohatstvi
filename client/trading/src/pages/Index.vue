<template>
  <q-page>
    <div class="container-sm">
      <div class="row">
        <div class="col text-center">
          <q-img alt="Quasar logo" width="50%" src="statics/motivation.jpg"></q-img>
        </div>
      </div>
      <div class="row">
        <div class="col text-center">
          <q-btn @click="func()">Klik</q-btn>
        </div>
      </div>
      <div class="row">
        <div class="col text-center">
          <blockquote class="blockquote">
  <p class="mb-0">Gádže nemíňajú loooove, <br>
Gádže nemíňajú looooveeee,<br>
Nechcú ma videť, jak sedím s vagošmi na káve, (káve) <br>
Nechcú ma videť ohrievať si fajnový bazén, (bazén, yeah)</p>
  <footer class="blockquote-footer">Rytmus <cite title="Source Title">Gádže nemíňajú lóve</cite></footer>
</blockquote>
        <blockquote>
          
        </blockquote>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "PageIndex",
  methods: {
    func(params) {
      this.$store.dispatch("global/buyProducts", this.products);
      this.count++;
      console.log(this.products);
    }
  },
  data() {
    return {
      count: 0,
      products: [
        {
          name: "zemiaky",
          num: 2,
          price: 10
        }
      ]
    };
  }
};
</script>
<style lang="css">
.img {
  position: fixed;
  top: 0;
  max-width: 50%;
}
</style>
