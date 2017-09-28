#!/usr/bin/env groovy

/**
 * Supports the "smartTesting { strategies = 'new, changed' }" syntax.
 */
def call(body) {
  def config = [:]
  if (body != null) {
      body.resolveStrategy = Closure.DELEGATE_FIRST
      body.delegate = config
      body()
  }
  call(config)
}

def call(Map config = [:]) {

  println "Using Next Configuration: ${config}"

  // Maven location
  def mvnCli = 'mvn'
  if (config.mvnHome != null) {
    mvnCli = "${config.mvnHome}/bin/mvn"
  }

  // Maven goals
  def goals = config.goals?: ' clean test'

  // Maven profiles
  def profiles = ''
  if (config.profiles != null) {
    profiles = "-P${config.profiles}"
  }

  // Smart Testing strategy
  def strategies = ''
  if (config.strategies != null) {
    strategies = "-Dsmart.testing='${config.strategies}'"
  }

  // Commit Range
  def commitRange = ''
  if (config.scmRange != null) {
    commitRange = "-Dsmart.testing.mode=ordering -Dscm.range.head=${config.scmRange.GIT_COMMIT} -Dscm.range.tail=${config.scmRange.GIT_PREVIOUS_COMMIT}"
  } else {
    commitRange = "-Dscm.last.changes=1"
  }

  // Extra params
  def extraParams = config.extraParams?: ''

  sh "${mvnCli} ${profiles} ${extraParams} ${strategies} ${commitRange} ${goals}"
}
