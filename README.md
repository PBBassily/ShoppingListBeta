# ShoppingListBeta

## Idea
 It is simple shopping list Android application, which is made in less than 48 hours
## Capabilities
1. User can search for varity of products to buy
2. User can see search suggestions if he/she mistyped a product
3. User can check items from the list, it will move down and come transparent
4. User can know the price of each item, as well as the expected final total
5. User can keep the list between sessions of app usage
6. User can sort items by categories which makes checing related items easier
7. User can delete item wheter checked or unchecked by swiping the item to the left
8. User can restore the UNDO button from the snackBar
9. Finally, user can reset the whole list
## Main Components
1. **SQLite** for list saving between sessions
2. **MaterialSearchView** library to provide powerful the search tool
3. **RecyclerView** to implement the list of items instead of *ListView*
## Future Work
1. Dynamic loading of porducts from API
2. Support many languages
3. Support multiple lists
4. Connect it with Firbase to keep user list(s) in *CloudStorage*
