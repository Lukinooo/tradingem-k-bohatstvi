<q-header class="">
          <q-btn dense flat round icon="menu" @click="left = !left" />
      </q-header>
    <q-drawer 
    v-model="left"
    overlay
    show-if-above 
    :width="200"  
    :breakpoint="400">
      <q-scroll-area
        style="height: calc(100% - 150px); margin-top: 150px; border-right: 1px solid #ddd"
      >
        <q-list padding>
          <q-item clickable v-ripple to="/game">
            <q-item-section avatar>
              <q-icon name="explore" />
            </q-item-section>

            <q-item-section>Map</q-item-section>
          </q-item>

          <q-item active clickable v-ripple to="/game/inventory">
            <q-item-section avatar>
              <q-icon name="local_mall" />
            </q-item-section>

            <q-item-section>Inventory</q-item-section>
          </q-item>

          <q-item clickable v-ripple to="/game/ladder">
            <q-item-section avatar>
              <q-icon name="search" />
            </q-item-section>

            <q-item-section>Leaderboards</q-item-section>
          </q-item>

          <q-item clickable v-ripple to="/">
            <q-item-section avatar>
              <q-icon name="exit_to_app" />
            </q-item-section>

            <q-item-section>Exit to app</q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>

      <q-img
        class="absolute-top"
        src="https://cdn.quasar.dev/img/material.png"
        style="height: 150px"
      >
        <div class="absolute-bottom bg-transparent">
          <q-avatar size="56px" class="q-mb-sm">
            <img src="https://cdn.quasar.dev/img/boy-avatar.png" />
          </q-avatar>
          <div class="text-weight-bold">{{nick}}</div>
        </div>
      </q-img>
    </q-drawer>

   <q-drawer side="right" ></q-drawer>