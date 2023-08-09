package learningapp.superior.org.Netscreens

class ChapterModel {

    var name: String? = null
    var image: String? = null
    var id: String? = null

    constructor() : this("", "", "") {}
    constructor(
        name: String?, image: String?, id: String?
    ) {

        this.name = name
        this.image = image
        this.id = id

    }
}