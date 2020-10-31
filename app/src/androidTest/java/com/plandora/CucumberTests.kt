package com.plandora

import cucumber.api.CucumberOptions

@CucumberOptions(
    features = ["features"],
    glue = ["com.sniper.bdd.cucumber.steps"],
    tags = ["@e2e", "@smoke"]
)
@SuppressWarnings("unused")
class CucumberTests