listView('seed-jobs') {
    jobs {
        regex '.*-seed$'
    }
    columns {
        status()
        name()
        lastSuccess()
        lastFailure()
        cronTrigger()
        buildButton()
    }
}
