class MenuDefinitions {

    constructor() {

        this.menuDefinitions = {
            "Module1": {
                name: 'Module1',
                title: 'ui.menu.first',
                index: 1,
                type: "menuitem"
            },
            "Module2": {
                name: 'Module2',
                title: 'ui.menu.second',
                index: 2,
                type: "menuitem"
            }
        }
    }

    getMenuDefns() {
        return this.menuDefinitions;
    }

    getRoutes(menus){
        return
    }
}

export  default new MenuDefinitions();