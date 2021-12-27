/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.h
  * @brief          : Header for main.c file.
  *                   This file contains the common defines of the application.
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2021 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under Ultimate Liberty license
  * SLA0044, the "License"; You may not use this file except in compliance with
  * the License. You may obtain a copy of the License at:
  *                             www.st.com/SLA0044
  *
  ******************************************************************************
  */
/* USER CODE END Header */

/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __MAIN_H
#define __MAIN_H

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "stm32f1xx_hal.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Exported types ------------------------------------------------------------*/
/* USER CODE BEGIN ET */

/* USER CODE END ET */

/* Exported constants --------------------------------------------------------*/
/* USER CODE BEGIN EC */

/* USER CODE END EC */

/* Exported macro ------------------------------------------------------------*/
/* USER CODE BEGIN EM */

/* USER CODE END EM */

/* Exported functions prototypes ---------------------------------------------*/
void Error_Handler(void);

/* USER CODE BEGIN EFP */

/* USER CODE END EFP */

/* Private defines -----------------------------------------------------------*/
#define LIGHT_ONBOARD_Pin GPIO_PIN_13
#define LIGHT_ONBOARD_GPIO_Port GPIOC
#define BEEP_Pin GPIO_PIN_14
#define BEEP_GPIO_Port GPIOC
#define CHEST_Pin GPIO_PIN_15
#define CHEST_GPIO_Port GPIOC
#define RCC_IN_Pin GPIO_PIN_0
#define RCC_IN_GPIO_Port GPIOD
#define RCC_OUT_Pin GPIO_PIN_1
#define RCC_OUT_GPIO_Port GPIOD
#define MOTOR_B_1_Pin GPIO_PIN_0
#define MOTOR_B_1_GPIO_Port GPIOA
#define LIGHT_A_Pin GPIO_PIN_1
#define LIGHT_A_GPIO_Port GPIOA
#define OPENMV_TX_Pin GPIO_PIN_2
#define OPENMV_TX_GPIO_Port GPIOA
#define OPENMV_RX_Pin GPIO_PIN_3
#define OPENMV_RX_GPIO_Port GPIOA
#define LIGHT_B_Pin GPIO_PIN_4
#define LIGHT_B_GPIO_Port GPIOA
#define OLED_SCK_Pin GPIO_PIN_5
#define OLED_SCK_GPIO_Port GPIOA
#define OLED_RST_Pin GPIO_PIN_6
#define OLED_RST_GPIO_Port GPIOA
#define OLED_MOSI_Pin GPIO_PIN_7
#define OLED_MOSI_GPIO_Port GPIOA
#define PWM_A_Pin GPIO_PIN_0
#define PWM_A_GPIO_Port GPIOB
#define OLED_CMD_Pin GPIO_PIN_1
#define OLED_CMD_GPIO_Port GPIOB
#define TX_Pin GPIO_PIN_10
#define TX_GPIO_Port GPIOB
#define RX_Pin GPIO_PIN_11
#define RX_GPIO_Port GPIOB
#define MOTOR_A_2_Pin GPIO_PIN_12
#define MOTOR_A_2_GPIO_Port GPIOB
#define KEY_1_Pin GPIO_PIN_13
#define KEY_1_GPIO_Port GPIOB
#define MOTOR_B_2_Pin GPIO_PIN_14
#define MOTOR_B_2_GPIO_Port GPIOB
#define KEY_2_Pin GPIO_PIN_15
#define KEY_2_GPIO_Port GPIOB
#define ENCODER_A_1_Pin GPIO_PIN_8
#define ENCODER_A_1_GPIO_Port GPIOA
#define ENCODER_A_2_Pin GPIO_PIN_9
#define ENCODER_A_2_GPIO_Port GPIOA
#define MOTOR_A_1_Pin GPIO_PIN_10
#define MOTOR_A_1_GPIO_Port GPIOA
#define SWDIO_Pin GPIO_PIN_13
#define SWDIO_GPIO_Port GPIOA
#define SWCLK_Pin GPIO_PIN_14
#define SWCLK_GPIO_Port GPIOA
#define ENCODER_B_1_Pin GPIO_PIN_15
#define ENCODER_B_1_GPIO_Port GPIOA
#define ENCODER_B_2_Pin GPIO_PIN_3
#define ENCODER_B_2_GPIO_Port GPIOB
#define PWM_B_Pin GPIO_PIN_4
#define PWM_B_GPIO_Port GPIOB
#define OLED_CS_Pin GPIO_PIN_5
#define OLED_CS_GPIO_Port GPIOB
#define ESP_TX_Pin GPIO_PIN_6
#define ESP_TX_GPIO_Port GPIOB
#define ESP_RX_Pin GPIO_PIN_7
#define ESP_RX_GPIO_Port GPIOB
#define MPU6050_SCL_Pin GPIO_PIN_8
#define MPU6050_SCL_GPIO_Port GPIOB
#define MPU6050_SDA_Pin GPIO_PIN_9
#define MPU6050_SDA_GPIO_Port GPIOB
/* USER CODE BEGIN Private defines */

/* USER CODE END Private defines */

#ifdef __cplusplus
}
#endif

#endif /* __MAIN_H */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
