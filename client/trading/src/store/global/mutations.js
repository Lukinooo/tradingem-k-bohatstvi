export function addToInv (state, {name,num,price}){
    state.inventory.push({
        name: name,
        num: num,
    })
}

export function buyProd (state, {name,num,price}) {
    state.inventory.forEach(element => {
        if (element.name === name){
            element.num += num;
        }
    });
    state.game.money -= num*price;
}

export function sellProd (state, {name, num, price}) {
    state.inventory.forEach(element => {
        if (element.name === name){
            element.num -= num;
        }
    });
    state.game.money += num*price;
}

export function name (state, name){
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
    state.game.num_players = num_players
}

export function duration(state, duration){
    state.game.duration = duration
}

export function shops(state, shops){
    state.game.shops = shops
}

