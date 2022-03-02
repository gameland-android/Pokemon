This project is meant to test Android development with MVI architecture and other features
### Description
***
This simple app shows a list of Pokemons with their image and name. When the user clicks on an element of the list, the app will show a new screen with more detailed info about that Pokemon (types, images, size, statistics)

### Additional features
***
- The main toolbar contains a search widget. When the user inserts a Pokemon name or a part of it, the list will update to show all the Pokemons that match the search
- While viewing a Pokemon's detailed view, the user can go directly to the previous or the next Pokemon in the list by clicking on the arrows that appear on the toolbar. The _back_ button always brings back to the Pokemon list
- Some views behave differently according to device orientation in order to better fit the screen

### Architecture
***
- MVI (Model-View-Intent) pattern
- Single Activity
- ViewModel
- Repository
- View binding

Inputs from the user will trigger _actions_ on the ViewModel.
The ViewModel (_MainDataFlow_) will update the _state_ of the app and send events.
The Activity and Fragments will react to changes in state and events. In particular: 
- _MainActivity_ will show the correct toolbar, manage the widgets of each toolbar and replace fragments
- _ListFragment_ will show the Pokemon list or an error message
- _PokemonFragment_ will show the Pokemon detailed info

Here is a diagram of all the states and events and how they are related
![app states diagram](https://www.game-land.it/pokemon/app_diagram.png)
_The states in blue show the Pokemon list, the states in red an empty list, the one in yellow a splash screen and the one in green Pokemon details_

### Libraries and technologies used
***
Language: Kotlin
MVI pattern: Uniflow library
Asynchronous calls: coroutines
Dependency injection: Koin
Network: Retrofit
JSON converter: Moshi
Image loading: Glide

### External resources
***
APIs from [PokéApi](https://pokeapi.co/)
Pokemon type icons from [Bulbapedia](https://bulbapedia.bulbagarden.net/wiki/Type)