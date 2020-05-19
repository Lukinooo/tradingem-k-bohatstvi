// export function someAction (/* context */) {
// }

export function change (state,name){
    state.commit('changeName',name)
    // console.log(state.nick);
}

export function buyProducts (state,products){
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
            // state.inventory.push({
                
            //     name: p.name,
            //     num: p.num
            // })
        }
        // state.money -= p.num * p.price
    }) 
}

export function sellProducts (state, products){
    products.forEach( p => {
        var found = false;
        state.inventory.forEach( s => {
            if (s.name === p.name){
                state.commit('sellProd',p)
                // s.num -= p.num;
                found = true;
            }
        })
        // state.money += p.num * p.price
    }) 
}

export function joinGame (state, game){

    state.commit('changeGame',game);
}

export function leaveGame (state){
    state.commit('inactiveGame');
}

export function initGame(state, data){
    return new Promise ((resolve,reject) =>{
        setTimeout(() =>{
            console.log('game init '+ JSON.stringify(data) + ' end '+ data.created_at + ' id ' + data.id);
            state.commit('addGameEnd',data.finished_at);
            state.commit('addGameStart',data.created_at);
            state.commit('setGameId',data.id);
            state.commit('activateGame');
            resolve()
        },1000)
    })
}

export function addShops(state, data){
    console.log('shops ' + JSON.stringify(data));
    state.commit('addShops',data)
}

export function setCenter(state, data){
    return new Promise ((resolve,reject) => {
        setTimeout(() => {
            state.commit('gps',data)
            resolve()
        },1000)
    })
    
}