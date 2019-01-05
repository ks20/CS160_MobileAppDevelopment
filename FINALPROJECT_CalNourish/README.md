# CalNourish

This is the repo for the CalNourish mobile application for **android devices**. This application is being made for the UC Berkeley Food Pantry.

API 28

Target SDK: 28

min SDK: 23

## Setting up
1. There are currently know required actions besides cloning the repo – clone and begin coding!
2. ???
3. Profit.

# Work flow
1. Create your own development branch
2. If you want to experiment, create another branch so we don't break things
3. When you feel comfortable, create a PR from your branch to master

# Project Stuff
1. MainActivity = CategoryActivity = our main screen when app appears
2. Bottom bar has:
	* Info
		+ just textbox with open hours and contancts and etc.
	* Category
		+ buttons that points to new activity with those items
	* Search
		+ goes to search bar now -- need to do the query stuff and history keeping etc.
	* Food Recovery
		+ empty
	* Menu
		+ has buttons that points to activities (has info category search for now)

# Webapp
Tutorial followed for setup: 
`https://codelabs.developers.google.com/codelabs/firebase-web/#0`

To run the webapp locally:

Setup:
`npm -g install firebase-tools`

`firebase login`

`firebase use --add`

From web directory: 
`firebase serve --only hosting`

`✔  hosting: Local server: http://localhost:5000`

To deploy:
`firebase deploy --except functions`

