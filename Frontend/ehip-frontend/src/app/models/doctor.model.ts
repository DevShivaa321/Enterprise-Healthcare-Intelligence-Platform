export interface Doctor {
  doctorId: number;

  firstName: string;

  lastName?: string; // makes lastname optional field

  specialization: string; // Optional field for years of experience

  phone: string; // Optional field for contact number

  email: string; // Optional field for email address
}
