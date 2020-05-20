// export function someAction (/* context */) {
// }

import { money } from "./mutations";
import { store } from "quasar/wrappers";

export function buyProduct (state,product){
    return new Promise ((resolve, reject) => {
        setTimeout(()=> {
            var found = false;

            // console.log('s ' + JSON.stringify(state) + 'prods '+product + 'inv '+ state.inventory)
            
            if (state.getters.money < product.price){
                reject('no money')
            }

            state.getters.inventory.forEach( s => {
                if (s.id === product.id){
                    state.commit('buyProd',product)
                    // s.num += p.num;
                    found = true;
                }
            })

            if (!found){
                state.commit('addToInv',product)
            }

            resolve(state.getters['game'].money)    
        },200)
    })
    
}

export function sellProduct (state, product){
    return new Promise ((resolve, reject) => {
        setTimeout(()=> {

            var found = false;

            state.getters.inventory.forEach( s => {
                if (s.id === product.id){
                    if (s.num === 0){
                        reject('not in inventory')
                    }
                    state.commit('sellProd',product)
                    // s.num -= p.num;
                    found = true;
                }
            })

            if (!found){
                reject('not in inventory')
            }

            resolve(state.getters['game'].money)
        },200)
    })
     
}

export function updateMoney(state, money){
    return new Promise((resolve,reject)=>{
        setTimeout(()=>{
            state.commit('money',money)
            resolve()
        },200)
    })
}

export function join(state, data){
    return new Promise ((resolve, reject) => {
        setTimeout(()=>{

            console.log('JOIN PLAYER ID before' + data.playerId + " DATA " + JSON.stringify(data) + ' store ' + state.getters['player_id'])
            state.commit('playerId',data.playerId)
            console.log('JOIN PLAYER ID after' + data.playerId + " DATA " + JSON.stringify(data) + ' store ' + state.getters['player_id']);

            resolve()
        },200)
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
            state.commit('money', parseInt(data.player_money,10));
            state.commit('addGameStart',data.created_at);
            state.commit('num_products',data.max_products);
            state.commit('radius',data.radius);
            state.commit('num_shops',data.max_shops);
            state.commit('color',data.color)
            console.log('INIT before data '+data.max_player+' state '+state.getters['num_players'])
            state.commit('num_players',data.max_player)
            console.log('INIT sfter data '+data.max_player+' state '+state.getters['num_players'])
            state.commit('duration',data.duration)
            state.commit('shops',data.shops)
            resolve()
        },200)
    })
}

export function setCenter(state, data){
    return new Promise ((resolve,reject) => {
        setTimeout(() => {
            state.commit('gps',data)
            resolve()
        },200)
    })   
}