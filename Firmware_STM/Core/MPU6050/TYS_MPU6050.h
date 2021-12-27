#include "stm32f1xx_hal.h"

unsigned char MPU_Set_LPF(unsigned short lpf);
void MPU_6050_Init(void);
void read_all(void);
unsigned char HAL_i2c_write(unsigned char slave_addr, unsigned char reg_addr, unsigned char length, unsigned char const *data);
unsigned char HAL_i2c_read(unsigned char slave_addr, unsigned char reg_addr, unsigned char length, unsigned char const *data);
unsigned short inv_row_2_scale(const signed char *row);
unsigned char run_self_test(void);
unsigned short inv_orientation_matrix_to_scalar(const signed char *mtx);
unsigned char mpu_dmp_init(void);
unsigned char mpu_dmp_get_data(float *pitch, float *roll, float *yaw);

