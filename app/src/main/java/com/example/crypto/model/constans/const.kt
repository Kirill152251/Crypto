package com.example.crypto.model.constans

const val QUERY_SORT_BY_PRICE = "price_desc"
const val QUERY_SORT_BY_MARKET_CAP = "market_cap_desc"
const val QUERY_SORT_BY_VOLATILITY = "price_change_percentage_24h_desc"
const val LABEL_DAY = "PER_DAY"
const val LABEL_WEEK = "PER_WEEK"
const val LABEL_MONTH = "PER_MONTH"
const val LABEL_YEAR = "PER_YEAR"
const val LABEL_ALL_TIME = "FOR_ALL_THE_TIME"
const val PREFERENCES_NAME = "preferences"
const val BROADCAST_STRING_FOR_ACTION = "CHECK_INTERNET"
const val ACTION_STOP_SERVICE = "STOP_SERVICE"
const val ONLINE_STATUS = "online_status"
const val COINS_DATABASE_NAME = "coins"
const val USER_INFO_DATABASE_NAME = "user_info"
const val DEFAULT_CURRENCY = "usd"
const val NETWORK_PAGE_SIZE = 20
const val MAX_INPUT_SIZE = 20
const val STARTING_PAGE_INDEX = 1
const val PROFILE_PHOTO_NAME = "photo.jpg"

enum class PagingSourceType{
    FROM_DB,
    SORT_BY_MARKET_CAP,
    SORT_BY_PRICE,
    SORT_BY_VOLATILITY
}

enum class SortBy {
    PRICE,
    MARKET_CAP,
    VOLATILITY
}

