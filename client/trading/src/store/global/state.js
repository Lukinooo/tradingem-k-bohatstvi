export default function () {
  return {
    game : {
      active : false,
      id : null,
      start : null,
      end : null,
      name: 'Pasívne príjmy',
      radius : 200,
      color : '#027be3',
      num_players : 5,
      num_shops : 5,
      num_products : 5,
      money : 1000,
      gps : {
        latitude : null,
        longitude : null,
      },
      shops : [
        {
          id : 1,
          gps : {
            latitude : null,
            longitude : null,
          },
        },
        {
          id : 2,
          gps : {
            latitude : null,
            longitude : null,
          },
        },
        {
          id : 3,
          gps : {
            latitude : null,
            longitude : null,
          },
        }
      ]
    },
    nick: "PU$$¥$$L4¥€R69",
    inventory : [
      {
        name : 'zemiaky',
        num : 2,
      },
      {
        name : 'psenica',
        num : 2,
      },
      {
        name : 'ovos',
        num : 2,
      },
      {
        name : 'musli',
        num : 2,
      }
    ]
  }
}
