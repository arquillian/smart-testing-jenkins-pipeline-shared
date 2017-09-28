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
  def goals = config.goals?:'clean test'
  echo "mvn ${goals}"
}
