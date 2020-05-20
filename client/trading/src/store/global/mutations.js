export function addToInv (state, product){
    state.inventory.push({
        id : product.id,
        name : product.name,
        num : 1,
    })
    state.game.money -= product.price;
}

export function buyProd (state, {id,price}) {
    state.inventory.forEach(element => {
        if (element.id === id){
            element.num += 1;
            state.game.money -= parseFloat(price);
            return
        }
    });
}

export function sellProd (state, {id, price}) {
    console.log('sell price ' + price);
    
    state.inventory.forEach(element => {
        if (element.id === id){
            if (element.num === 0){
                return
            }
            console.log('sell one id' + id +' comp ' + element.id);
            element.num = element.num - 1;
            console.log('sell money before' + JSON.stringify(state.game.money));
            state.game.money = parseFloat(state.game.money) +  parseFloat(price,10);
            console.log('sell money after' + JSON.stringify(state.game.money));
            return
        }
    });
    
}

export function name (state, name){
    state.game.name = name;
}

export function nick (state, name){
    state.nick = name;
}

export function changeGame (state, game){
    state.game = game;
}

export function inactiveGame(state){
    state.game.active = false;
}

export function addGameStart(state, start){
    state.game.created_at = start;
}

export function addGameEnd(state, end){
    state.game.finished_at = end;
}

export function activateGame(state){
    state.game.active = true;
}

export function setGameId(state,id){
    state.game.id = id;
}

export function gps(state, data){
    state.game.gps.longitude = data.longitude_center;
    state.game.gps.latitude = data.latitude_center;
}

export function money(state, money){
    state.game.money = money;
}

export function num_products(state, prods){
    state.game.num_products = prods;
}

export function radius(state, radius){
    state.game.radius = radius;
}

export function num_shops(state, num){
    state.game.num_shops = num
}

export function color(state, color){
    state.game.color = color
}

export function num_players(state, num){
    state.game.num_players = num
}

export function duration(state, duration){
    state.game.duration = duration
}

export function shops(state, shops){
    state.game.shops = shops
}

export function playerId(state,id){
    console.log('COMMIT before join id' + id +' store ' + state.game.player_id);
    
    state.player_id = id
    state.game.player_id = id

    console.log('COMMIT after join id' + id +' store ' + state.game.player_id);
}