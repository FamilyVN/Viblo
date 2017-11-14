package com.asia.viblo.model

/**
 * Created by anhtv on 14/11/2017.
 */
class ContentChild {
    var typeContent: TypeContent = TypeContent.TEXT
    var content = ""

    constructor(content: String) : this(content, TypeContent.TEXT)

    constructor(content: String, typeContent: TypeContent) {
        this.content = content
        this.typeContent = typeContent
    }
}