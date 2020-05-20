export function inventory (state) {
    return state.inventory
}

export function game (state) {
    return state.game
}

export function nick (state) {
    return state.nick
}

export function player_id(state){
    console.log('GETTERS join player_id ' + state.player_id)
    return state.player_id
}