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

export function changeName (state, name){
    state.nick = name;
}

export function changeGame (state, game){
    state.game = game;
}

export function inactiveGame(state){
    state.game.active = false;
}