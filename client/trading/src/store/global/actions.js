// export function someAction (/* context */) {
// }

import { money } from "./mutations";

export function change (state,name){
    state.commit('changeName',name)
    // console.log(state.nick);
}

export function buyProducts (state,products){
    return new Promise ((resolve, reject) => {
        setTimeout(()=> {
            products.forEach( p => {
                var found = false;
                console.log('s ' + JSON.stringify(state) + 'prods '+products + 'inv '+ state.inventory)
                state.getters.inventory.forEach( s => {
                    if (s.name === p.name){
                        state.commit('buyProd',p)
                        // s.num += p.num;
                        found = true;
                    }
                })
                if (!found){
                    state.commit('addToInv',p)
                }
            })
            resolve(state.getters['global/game'].money) 
        },1000)
    })
    
}

export function sellProducts (state, products){
    return new Promise ((resolve, reject) => {
        setTimeout(()=> {
            products.forEach( p => {
                var found = false;
                state.inventory.forEach( s => {
                    if (s.name === p.name){
                        state.commit('sellProd',p)
                        // s.num -= p.num;
                        found = true;
                    }
                })
            })
            resolve(state.getters(money))
        },1000)
    })
     
}

export function join(state, data){
    return new Promise ((resolve, reject) => {
        setTimeout(()=>{
            console.log('JOIN PLAYER ID' + data.playerId + " DATA " + JSON.stringify(data))
            state.commit('playerId',data.playerId)
            resolve()
        })
    })
}

export function leaveGame (state){
    state.commit('inactiveGame');
}

export function initGame(state, data){
    return new Promise ((resolve,reject) =>{
        setTimeout(() =>{
            console.log('game init '+ JSON.stringify(data) + ' end '+ data.created_at + ' id ' + data.id);
            state.commit('activateGame');
            state.commit('name',data.name);
            state.commit('setGameId',data.id);
            state.commit('gps',data);
            state.commit('addGameEnd',data.finished_at);
            state.commit('money',data.player_money);
            state.commit('addGameStart',data.created_at);
            state.commit('num_products',data.max_products);
            state.commit('radius',data.radius);
            state.commit('num_shops',data.max_shops);
            state.commit('color',data.color)
            state.commit('num_players',data.max_player)
            state.commit('duration',data.duration)
            state.commit('shops',data.shops)
            resolve()
        },1000)
    })
}

export function setCenter(state, data){
    return new Promise ((resolve,reject) => {
        setTimeout(() => {
            state.commit('gps',data)
            resolve()
        },1000)
    })
    
}