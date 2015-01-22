/**
 * Controller for Rules tab.
 */

angular
  .module('pipelineAgentApp.home')

  .controller('RulesController', function ($scope) {
    angular.extend($scope, {
      showLoading: false,

      /**
       * Refresh Rules
       */
      refreshRules: function() {
        updateRules($scope.activeConfigInfo.name);
      },

      /**
       * Callback function for Create New Alert Rule button.
       *
       */
      createAlertRule: function() {
        var edge =  $scope.selectedObject,
          newAlertDefn = {
            id: edge.outputLane + (new Date()).getTime(),
            label: '',
            lane: edge.outputLane,
            predicate: '',
            thresholdType: 'COUNT',
            thresholdValue: null,
            minVolume: null,
            enabled: false
          };

        $scope.pipelineRules.alertDefinitions.push(newAlertDefn);
      },

      /**
       * Callback function for Create New Metric Rule button.
       */
      createMetricRule: function() {
        var edge =  $scope.selectedObject,
          newMetricDefn = {
            id: edge.outputLane + (new Date()).getTime(),
            label: '',
            metricGroup: '',
            metricType: 'METER',
            lane: edge.outputLane,
            predicate: '',
            enabled: false
          };

        $scope.pipelineRules.metricDefinitions.push(newMetricDefn);
      },

      /**
       * Callback function for Create New Sampling Rule button.
       */
      createSamplingRule: function() {
        var edge =  $scope.selectedObject,
          newSamplingDefn = {
            id: edge.outputLane + (new Date()).getTime(),
            label: '',
            lane: edge.outputLane,
            predicate: '',
            samplingPercentage: '',
            enabled: false
          };

        $scope.pipelineRules.samplingDefinitions.push(newSamplingDefn);
      },

      /**
       * Callback function for Create New Metric Alert Rule button.
       */
      createMetricAlertRule: function() {

      },

      /**
       * Remove Callback function
       *
       * @param ruleList
       * @param $index
       */
      removeRule: function(ruleList, $index) {
        ruleList.splice($index, 1);
      }
    });

    var updateRules = function(pipelineName) {
      $scope.showLoading = true;
      $scope.showLoading = false;
    };

    updateRules();
  });