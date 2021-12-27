/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * File Name          : freertos.c
  * Description        : Code for freertos applications
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

/* Includes ------------------------------------------------------------------*/
#include "FreeRTOS.h"
#include "task.h"
#include "main.h"
#include "cmsis_os.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include "adc.h"
#include "i2c.h"
#include "spi.h"
#include "usart.h"
#include "tim.h"
#include "TYS_OLED.h"
#include "usbd_cdc_if.h"
#include "../MPU6050/TYS_MPU6050.h"
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */

/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
/* USER CODE BEGIN Variables */
unsigned char UART1_Rx_Buf[8] = {0x00}; //USART1存储接收数据
unsigned char UART1_Rx_flg = 0;         //USART1接收完成标志
unsigned int  UART1_Rx_cnt = 0;         //USART1接受数据计数�?
unsigned char UART1_temp[1] = {0x00};   //USART1接收数据缓存

unsigned char UART2_Rx_Buf[8] = {0x00}; //USART2存储接收数据
unsigned char UART2_Rx_flg = 0;         //USART2接收完成标志
unsigned int  UART2_Rx_cnt = 0;         //USART2接受数据计数�?
unsigned char UART2_temp[1] = {0x00};   //USART2接收数据缓存

unsigned char UART3_Rx_Buf[8] = {0x00}; //USART3存储接收数据
unsigned char UART3_Rx_flg = 0;         //USART3接收完成标志
unsigned int  UART3_Rx_cnt = 0;         //USART3接受数据计数�?
unsigned char UART3_temp[1] = {0x00};   //USART3接收数据缓存

int32_t Light_A, Light_B;
int32_t Speed_A, Speed_B;
int32_t ERR_A_Last_0, ERR_B_Last_0;
int32_t ERR_A_Last_1, ERR_B_Last_1;
int32_t ERR_A_Last_2, ERR_B_Last_2;
int32_t Speed_Target_A, Speed_Target_B;
int32_t PWM_A, PWM_B;
int32_t PWM_A_Bias, PWM_B_Bias;
int32_t PID_P = 400, PID_I = 800, PID_D = 100;

int32_t Length_A, Length_B;
uint8_t FLAG_MOVE;

uint8_t Key_1, Key_2;

uint8_t OpenMV_Num, OpenMV_Dir;

float X, Y, Z;

//uint8_t UART1_Rx_Buf[8]; //USART1存储接收数据
//uint8_t UART2_Rx_Buf[8] = "XXXXXXXX"; //USART2存储接收数据
//uint8_t UART3_Rx_Buf[8]; //USART3存储接收数据


enum DIRECTION {
    DIR_STOP = 0,
    DIR_FORWARD = 1,
    DIR_BACKWARD = 2,
    DIR_LEFT = 3,
    DIR_RIGHT = 4
};
enum DIRECTION Direction = DIR_STOP;
/* USER CODE END Variables */
osThreadId defaultTaskHandle;
uint32_t defaultTaskBuffer[ 128 ];
osStaticThreadDef_t defaultTaskControlBlock;
osThreadId Screen_RefresheHandle;
uint32_t Screen_RefresheBuffer[ 128 ];
osStaticThreadDef_t Screen_RefresheControlBlock;
osThreadId ADC_ReadHandle;
uint32_t ADC_ReadBuffer[ 128 ];
osStaticThreadDef_t ADC_ReadControlBlock;
osThreadId Speed_ControlHandle;
uint32_t Speed_ControlBuffer[ 128 ];
osStaticThreadDef_t Speed_ControlControlBlock;

/* Private function prototypes -----------------------------------------------*/
/* USER CODE BEGIN FunctionPrototypes */
void CAR_RUN(int32_t Length_R, int32_t Length_L, int32_t Target_R, int32_t Target_L, enum DIRECTION Dir) {
    Length_A = Length_R;
    Length_B = Length_L;
    Speed_Target_A = Target_R;
    Speed_Target_B = Target_L;
    Direction = Dir;
    HAL_GPIO_WritePin(LIGHT_ONBOARD_GPIO_Port, LIGHT_ONBOARD_Pin, GPIO_PIN_RESET);
    FLAG_MOVE = 1;
}
void HAL_UART_RxCpltCallback(UART_HandleTypeDef *huart)
{
    if(huart->Instance==USART1)
    {
        UART1_Rx_Buf[UART1_Rx_cnt] = UART1_temp[0];
        CDC_Transmit_FS(UART1_temp, 1);
        UART1_Rx_cnt++;
        if(0x7f == UART1_temp[0])
        {
            UART1_Rx_flg = 1;
        }
        HAL_UART_Receive_IT(&huart1,(uint8_t *)UART1_temp,1);
    }
    if(huart->Instance==USART2)
    {
        UART2_Rx_Buf[UART2_Rx_cnt] = UART2_temp[0];
        CDC_Transmit_FS(UART2_temp, 1);
        UART2_Rx_cnt++;
        if(0x7f == UART2_temp[0])
        {
            UART2_Rx_flg = 1;
        }
        HAL_UART_Receive_IT(&huart2,(uint8_t *)UART2_temp,1);
    }
    if(huart->Instance==USART3)
    {
        UART3_Rx_Buf[UART3_Rx_cnt] = UART3_temp[0];
        CDC_Transmit_FS(UART3_temp, 1);
        UART3_Rx_cnt++;
        if(0x7f == UART3_temp[0])
        {
            UART3_Rx_flg = 1;
        }
        HAL_UART_Receive_IT(&huart3,(uint8_t *)UART3_temp,1);
    }
}
/* USER CODE END FunctionPrototypes */

void StartDefaultTask(void const * argument);
void Screen_Refresh(void const * argument);
void ADC_Get(void const * argument);
void Control_Speed(void const * argument);

extern void MX_USB_DEVICE_Init(void);
void MX_FREERTOS_Init(void); /* (MISRA C 2004 rule 8.1) */

/* GetIdleTaskMemory prototype (linked to static allocation support) */
void vApplicationGetIdleTaskMemory( StaticTask_t **ppxIdleTaskTCBBuffer, StackType_t **ppxIdleTaskStackBuffer, uint32_t *pulIdleTaskStackSize );

/* USER CODE BEGIN GET_IDLE_TASK_MEMORY */
static StaticTask_t xIdleTaskTCBBuffer;
static StackType_t xIdleStack[configMINIMAL_STACK_SIZE];

void vApplicationGetIdleTaskMemory( StaticTask_t **ppxIdleTaskTCBBuffer, StackType_t **ppxIdleTaskStackBuffer, uint32_t *pulIdleTaskStackSize )
{
  *ppxIdleTaskTCBBuffer = &xIdleTaskTCBBuffer;
  *ppxIdleTaskStackBuffer = &xIdleStack[0];
  *pulIdleTaskStackSize = configMINIMAL_STACK_SIZE;
  /* place for user code */
}
/* USER CODE END GET_IDLE_TASK_MEMORY */

/**
  * @brief  FreeRTOS initialization
  * @param  None
  * @retval None
  */
void MX_FREERTOS_Init(void) {
  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* USER CODE BEGIN RTOS_MUTEX */
  /* add mutexes, ... */
  /* USER CODE END RTOS_MUTEX */

  /* USER CODE BEGIN RTOS_SEMAPHORES */
  /* add semaphores, ... */
  /* USER CODE END RTOS_SEMAPHORES */

  /* USER CODE BEGIN RTOS_TIMERS */
  /* start timers, add new ones, ... */
  /* USER CODE END RTOS_TIMERS */

  /* USER CODE BEGIN RTOS_QUEUES */
  /* add queues, ... */
  /* USER CODE END RTOS_QUEUES */

  /* Create the thread(s) */
  /* definition and creation of defaultTask */
  osThreadStaticDef(defaultTask, StartDefaultTask, osPriorityNormal, 0, 128, defaultTaskBuffer, &defaultTaskControlBlock);
  defaultTaskHandle = osThreadCreate(osThread(defaultTask), NULL);

  /* definition and creation of Screen_Refreshe */
  osThreadStaticDef(Screen_Refreshe, Screen_Refresh, osPriorityNormal, 0, 128, Screen_RefresheBuffer, &Screen_RefresheControlBlock);
  Screen_RefresheHandle = osThreadCreate(osThread(Screen_Refreshe), NULL);

  /* definition and creation of ADC_Read */
  osThreadStaticDef(ADC_Read, ADC_Get, osPriorityNormal, 0, 128, ADC_ReadBuffer, &ADC_ReadControlBlock);
  ADC_ReadHandle = osThreadCreate(osThread(ADC_Read), NULL);

  /* definition and creation of Speed_Control */
  osThreadStaticDef(Speed_Control, Control_Speed, osPriorityHigh, 0, 128, Speed_ControlBuffer, &Speed_ControlControlBlock);
  Speed_ControlHandle = osThreadCreate(osThread(Speed_Control), NULL);

  /* USER CODE BEGIN RTOS_THREADS */
  /* add threads, ... */
  /* USER CODE END RTOS_THREADS */

}

/* USER CODE BEGIN Header_StartDefaultTask */
/**
  * @brief  Function implementing the defaultTask thread.
  * @param  argument: Not used
  * @retval None
  */
/* USER CODE END Header_StartDefaultTask */
void StartDefaultTask(void const * argument)
{
  /* init code for USB_DEVICE */
  MX_USB_DEVICE_Init();
  /* USER CODE BEGIN StartDefaultTask */
    HAL_UART_Receive_IT(&huart1,(uint8_t *)UART1_temp,1);
    HAL_UART_Receive_IT(&huart2,(uint8_t *)UART2_temp,1);
    HAL_UART_Receive_IT(&huart3,(uint8_t *)UART3_temp,1);
//    osDelay(1000);
//    CAR_RUN(7500, 7500, 50, 50, DIR_FORWARD);
//    while (FLAG_MOVE);
//    osDelay(500);
//    CAR_RUN(7500, 7500, 50, 50, DIR_BACKWARD);
//    while (FLAG_MOVE);
//    osDelay(500);
//    CAR_RUN(7500, 7500, 50, 50, DIR_RIGHT);
//    while (FLAG_MOVE);
//    osDelay(500);
//    CAR_RUN(7500, 7500, 50, 50, DIR_LEFT);
//    while (FLAG_MOVE);
//    CAR_RUN(2000, 2000, 50, 50, DIR_FORWARD);
  /* Infinite loop */
    for (;;) {
        osDelay(100);
    }
  /* USER CODE END StartDefaultTask */
}

/* USER CODE BEGIN Header_Screen_Refresh */
/**
* @brief Function implementing the Screen_Refreshe thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_Screen_Refresh */
void Screen_Refresh(void const * argument)
{
  /* USER CODE BEGIN Screen_Refresh */
    OLED_Init();
    OLED_Clear();
    OLED_Display_On();
  /* Infinite loop */
    for (;;) {
        if (Key_1 == 1) {
            if (Key_2 == 1) {
                OLED_ShowString(0, 0, "     LIGHT     ");
                OLED_ShowString(0, 2, "LightA: ");
                OLED_ShowString(0, 4, "LightB: ");
                OLED_ShowString(0, 6, "               ");
                OLED_ShowNum(65, 2, Light_A, 4, 16);
                OLED_ShowNum(65, 4, Light_B, 4, 16);
            }
            if (Key_2 == 0) {
                OLED_ShowString(0, 0, "     SPEED     ");
                OLED_ShowString(0, 2, "SpeedA: ");
                OLED_ShowString(0, 4, "SpeedB: ");
                OLED_ShowString(0, 6, "               ");
                OLED_ShowNum(65, 2, Speed_A, 4, 16);
                OLED_ShowNum(65, 4, Speed_B, 4, 16);
            }
        }
        if (Key_1 == 0) {
            if (Key_2 == 1) {
                OLED_ShowString(0, 0, "    OPEN_MV    ");
                OLED_ShowString(0, 2, "NUMBER: ");
                OLED_ShowString(0, 4, "DIRECT: ");
                OLED_ShowString(0, 6, "               ");
                OLED_ShowNum(65, 2, OpenMV_Num, 4, 16);
                OLED_ShowNum(65, 4, OpenMV_Dir, 4, 16);
            }
            if (Key_2 == 0) {
                OLED_ShowString(0, 0, "    MPU6050    ");
                if (X >= 0) OLED_ShowString(0, 2, "ANG_X : ");
                else OLED_ShowString(0, 2, "ANG_X :-");
                if (Y >= 0) OLED_ShowString(0, 4, "ANG_Y : ");
                else OLED_ShowString(0, 4, "ANG_Y :-");
                if (Z >= 0) OLED_ShowString(0, 6, "ANG_Z : ");
                else OLED_ShowString(0, 6, "ANG_Z :");
                OLED_ShowNum(65, 2, abs(X*10), 4, 16);
                OLED_ShowNum(65, 4, abs(Y*10) * 10, 4, 16);
                OLED_ShowNum(65, 6, abs(Z*10), 4, 16);
            }
        }
        osDelay(40);
    }
  /* USER CODE END Screen_Refresh */
}

/* USER CODE BEGIN Header_ADC_Get */
/**
* @brief Function implementing the ADC_Read thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_ADC_Get */
void ADC_Get(void const * argument)
{
  /* USER CODE BEGIN ADC_Get */
    uint8_t USB_Send[] = "SpeedA=   ,SpeedB=   \n";
    uint8_t NUM_Table[] = "0123456789";
    uint8_t UART1_Send[8] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    //MPU_6050_Init();
    //while (mpu_dmp_init());
  /* Infinite loop */
    for (;;) {

        if(UART1_Rx_flg)
        {
            switch (UART1_Rx_Buf[0]) {
                case 0:
                    Direction = DIR_STOP;
                    Speed_Target_A = UART1_Rx_Buf[1] * 128 + UART1_Rx_Buf[2];
                    Speed_Target_B = UART1_Rx_Buf[3] * 128 + UART1_Rx_Buf[4];
                    break;
                case 1:
                    Direction = DIR_FORWARD;
                    Speed_Target_A = UART1_Rx_Buf[1] * 128 + UART1_Rx_Buf[2];
                    Speed_Target_B = UART1_Rx_Buf[3] * 128 + UART1_Rx_Buf[4];
                    break;
                case 2:
                    Direction = DIR_LEFT;
                    Speed_Target_A = UART1_Rx_Buf[1] * 128 + UART1_Rx_Buf[2];
                    Speed_Target_B = UART1_Rx_Buf[3] * 128 + UART1_Rx_Buf[4];
                    break;
                case 3:
                    Direction = DIR_RIGHT;
                    Speed_Target_A = UART1_Rx_Buf[1] * 128 + UART1_Rx_Buf[2];
                    Speed_Target_B = UART1_Rx_Buf[3] * 128 + UART1_Rx_Buf[4];
                    break;
                case 4:
                    Direction = DIR_BACKWARD;
                    Speed_Target_A = UART1_Rx_Buf[1] * 128 + UART1_Rx_Buf[2];
                    Speed_Target_B = UART1_Rx_Buf[3] * 128 + UART1_Rx_Buf[4];
                    break;
                case 5:
                    PID_P = UART1_Rx_Buf[1] * 128 + UART1_Rx_Buf[2];
                    PID_I = UART1_Rx_Buf[3] * 128 + UART1_Rx_Buf[4];
                    PID_D = UART1_Rx_Buf[5] * 128 + UART1_Rx_Buf[6];
                default:
                    Direction = DIR_STOP;

            }
            for(int i = 0;i<UART1_Rx_cnt;i++) {
                UART1_Rx_Buf[i] = 0;
            }
            UART1_Rx_cnt = 0;
            UART1_Rx_flg = 0;
        }


        if(UART2_Rx_flg)
        {
            OpenMV_Num = UART2_Rx_Buf[0];
            OpenMV_Dir = UART2_Rx_Buf[1];
            for(int i = 0;i<UART2_Rx_cnt;i++) {
                UART2_Rx_Buf[i] = 0;
            }
            UART2_Rx_cnt = 0;
            UART2_Rx_flg = 0;
        }


        if(UART3_Rx_flg)
        {
            for(int i = 0;i<UART3_Rx_cnt;i++) {
                UART3_Rx_Buf[i] = 0;
            }
            UART3_Rx_cnt = 0;
            UART3_Rx_flg = 0;
        }


        mpu_dmp_get_data(&X, &Y, &Z);

        HAL_ADC_Start(&hadc1);
        HAL_ADC_Start(&hadc2);
        Light_A = HAL_ADC_GetValue(&hadc1);
        Light_B = HAL_ADC_GetValue(&hadc2);

        Key_1 = HAL_GPIO_ReadPin(KEY_1_GPIO_Port, KEY_1_Pin);
        Key_2 = HAL_GPIO_ReadPin(KEY_2_GPIO_Port, KEY_2_Pin);

        USB_Send[7] = NUM_Table[Speed_A/100];
        USB_Send[8] = NUM_Table[(Speed_A/10)%10];
        USB_Send[9] = NUM_Table[Speed_A%10];

        USB_Send[18] = NUM_Table[Speed_B/100];
        USB_Send[19] = NUM_Table[(Speed_B/10)%10];
        USB_Send[20] = NUM_Table[Speed_B%10];

        //CDC_Transmit_FS(USB_Send, sizeof USB_Send-1);
        UART1_Send[1] = Speed_A / 128;
        UART1_Send[2] = Speed_A % 128;
        UART1_Send[3] = Speed_B / 128;
        UART1_Send[4] = Speed_B % 128;
        HAL_UART_Transmit_IT(&huart1,UART1_Send,8);
        osDelay(20);
    }
  /* USER CODE END ADC_Get */
}

/* USER CODE BEGIN Header_Control_Speed */
/**
* @brief Function implementing the Speed_Control thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_Control_Speed */
void Control_Speed(void const * argument)
{
  /* USER CODE BEGIN Control_Speed */
    HAL_TIM_Encoder_Start(&htim1, TIM_CHANNEL_3);
    HAL_TIM_Encoder_Start(&htim1, TIM_CHANNEL_4);
    HAL_TIM_Encoder_Start(&htim2, TIM_CHANNEL_3);
    HAL_TIM_Encoder_Start(&htim2, TIM_CHANNEL_4);

    HAL_TIM_PWM_Start(&htim3, TIM_CHANNEL_1);
    HAL_TIM_PWM_Start(&htim3, TIM_CHANNEL_3);



    int32_t temp = 0;
  /* Infinite loop */
    for (;;) {

        if (Length_A < 0) {
            Speed_Target_A = 0;
            if(Length_B < 0){
                Length_A = 0;
                Length_B = 0;
                Speed_Target_B = 0;
                FLAG_MOVE = 0;
                HAL_GPIO_WritePin(LIGHT_ONBOARD_GPIO_Port, LIGHT_ONBOARD_Pin, GPIO_PIN_SET);
                Direction = DIR_STOP;
            }
        }


        switch (Direction) {
            case DIR_STOP:
                PWM_A = 0;
                PWM_B = 0;
                Speed_Target_A = 0;
                Speed_Target_B = 0;
                HAL_GPIO_WritePin(MOTOR_A_1_GPIO_Port, MOTOR_A_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_A_2_GPIO_Port, MOTOR_A_2_Pin, GPIO_PIN_SET);

                HAL_GPIO_WritePin(MOTOR_B_1_GPIO_Port, MOTOR_B_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_B_2_GPIO_Port, MOTOR_B_2_Pin, GPIO_PIN_SET);
                break;
            case DIR_FORWARD:
                HAL_GPIO_WritePin(MOTOR_A_1_GPIO_Port, MOTOR_A_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_A_2_GPIO_Port, MOTOR_A_2_Pin, GPIO_PIN_RESET);

                HAL_GPIO_WritePin(MOTOR_B_1_GPIO_Port, MOTOR_B_1_Pin, GPIO_PIN_RESET);
                HAL_GPIO_WritePin(MOTOR_B_2_GPIO_Port, MOTOR_B_2_Pin, GPIO_PIN_SET);
                break;
            case DIR_BACKWARD:
                HAL_GPIO_WritePin(MOTOR_A_1_GPIO_Port, MOTOR_A_1_Pin, GPIO_PIN_RESET);
                HAL_GPIO_WritePin(MOTOR_A_2_GPIO_Port, MOTOR_A_2_Pin, GPIO_PIN_SET);

                HAL_GPIO_WritePin(MOTOR_B_1_GPIO_Port, MOTOR_B_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_B_2_GPIO_Port, MOTOR_B_2_Pin, GPIO_PIN_RESET);
                break;
            case DIR_LEFT:
                HAL_GPIO_WritePin(MOTOR_A_1_GPIO_Port, MOTOR_A_1_Pin, GPIO_PIN_RESET);
                HAL_GPIO_WritePin(MOTOR_A_2_GPIO_Port, MOTOR_A_2_Pin, GPIO_PIN_SET);

                HAL_GPIO_WritePin(MOTOR_B_1_GPIO_Port, MOTOR_B_1_Pin, GPIO_PIN_RESET);
                HAL_GPIO_WritePin(MOTOR_B_2_GPIO_Port, MOTOR_B_2_Pin, GPIO_PIN_SET);
                break;
            case DIR_RIGHT:
                HAL_GPIO_WritePin(MOTOR_A_1_GPIO_Port, MOTOR_A_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_A_2_GPIO_Port, MOTOR_A_2_Pin, GPIO_PIN_RESET);

                HAL_GPIO_WritePin(MOTOR_B_1_GPIO_Port, MOTOR_B_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_B_2_GPIO_Port, MOTOR_B_2_Pin, GPIO_PIN_RESET);
                break;
            default:
                HAL_GPIO_WritePin(MOTOR_A_1_GPIO_Port, MOTOR_A_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_A_2_GPIO_Port, MOTOR_A_2_Pin, GPIO_PIN_SET);

                HAL_GPIO_WritePin(MOTOR_B_1_GPIO_Port, MOTOR_B_1_Pin, GPIO_PIN_SET);
                HAL_GPIO_WritePin(MOTOR_B_2_GPIO_Port, MOTOR_B_2_Pin, GPIO_PIN_SET);
                break;
        }

        temp = __HAL_TIM_GET_COUNTER(&htim1);
        if (temp < 10000) Speed_A = temp;
        else Speed_A = 65535 - temp;
        temp = __HAL_TIM_GET_COUNTER(&htim2);
        if (temp < 10000) Speed_B = temp;
        else Speed_B = 65535 - temp;

        ERR_A_Last_2 = ERR_A_Last_1;
        ERR_A_Last_1 = ERR_A_Last_0;
        ERR_A_Last_0 = Speed_Target_A - Speed_A;

        ERR_B_Last_2 = ERR_B_Last_1;
        ERR_B_Last_1 = ERR_B_Last_0;
        ERR_B_Last_0 = Speed_Target_B - Speed_B;


        PWM_A += 0.001 * (PID_P * (ERR_A_Last_0 - ERR_A_Last_1) + PID_I * ERR_A_Last_0 + PID_D * (ERR_A_Last_0 - 2 * ERR_A_Last_1 + ERR_A_Last_2));
        PWM_B += 0.001 * (PID_P * (ERR_B_Last_0 - ERR_B_Last_1) + PID_I * ERR_B_Last_0 + PID_D * (ERR_B_Last_0 - 2 * ERR_B_Last_1 + ERR_B_Last_2));


        if(FLAG_MOVE) Length_A -= Speed_A;
        if(FLAG_MOVE) Length_B -= Speed_B;
        PWM_A_Bias = 0;
        PWM_B_Bias = 0;
                __HAL_TIM_SetCompare(&htim3, TIM_CHANNEL_3, PWM_A + PWM_A_Bias);
                __HAL_TIM_SetCompare(&htim3, TIM_CHANNEL_1, PWM_B + PWM_B_Bias);



        __HAL_TIM_SET_COUNTER(&htim1, 0);
        __HAL_TIM_SET_COUNTER(&htim2, 0);
        osDelay(50);
    }
  /* USER CODE END Control_Speed */
}

/* Private application code --------------------------------------------------*/
/* USER CODE BEGIN Application */

/* USER CODE END Application */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
